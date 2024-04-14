package dzr.transaction.init;

import dzr.common.utils.DateUtils;
import dzr.common.utils.HttpClientService;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.transaction.entity.TranDetail;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TranDetailMapper;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author dzr
 * @Date 2022/9/19 15:00
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SinaTranDetailInitData {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final TranDetailMapper tranDetailMapper;
    private final TransactionMapper transactionMapper;

    private static String url = "http://market.finance.sina.com.cn/transHis.php?symbol=$code&date=$reportDate&page=$page";

    private static int DAYS = 5;

    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            remountByCode(securityCode);
        });
    }

    public void remountByCode(SecurityCode securityCode) {
        ArrayList<TranDetail> tranDetails = new ArrayList<>();
        List<Date> inReportDates = tranDetailMapper.groupByCodeAndReportDate(securityCode.getCode());
        HashMap<Long, Date> dateHashMap = new HashMap<Long, Date>();
        inReportDates.forEach(inReportDate -> dateHashMap.put(inReportDate.getTime(), inReportDate));

        List<Transaction> transactions = transactionMapper.selectReinstatementByCode(securityCode.getCode());
        transactions = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getReportDate).reversed()).collect(Collectors.toList());

        for (int i = 0; i < DAYS && i < transactions.size(); i++) {
            Date reportDate = transactions.get(i).getReportDate();
            if( dateHashMap.get(reportDate.getTime()) != null){
                continue;
            }

            for (int m = 0; m < 100; m++) {
                String urlPre = url.replace("$code", securityCode.getCodeWithExchange())
                        .replace("$reportDate", DateUtils.dateToStr(reportDate, "yyyy-MM-dd"))
                        .replace("$page", String.valueOf(m));
                log.info("url : {}", urlPre);

                String ret = httpClientService.doGet(urlPre);
                Document document = Jsoup.parse(ret);
                Elements tbody = document.getElementsByTag("tbody");
                if (tbody.size() == 1) {
                    Element element = tbody.get(0);
                    Elements tr = element.getElementsByTag("tr");
                    if (tr.size() == 0) {
                        log.info("table表数据为空");
                        break;
                    }

                    for (int j = 0; j < tr.size(); j++) {
                        Element row = tr.get(j);
                        Elements children = row.children();
                        TranDetail tranDetail = new TranDetail();

                        tranDetail.setCode(securityCode.getCode());
                        tranDetail.setReportDate(reportDate);
                        tranDetail.setTranTime(DateUtils.strToDate(children.get(0).text(), "HH:mm:ss"));
                        tranDetail.setPrice(Double.parseDouble(children.get(1).text()));
                        tranDetail.setChangePrice(Double.parseDouble(children.get(2).text().replace("--", "0")));
                        tranDetail.setChangedHands(Long.parseLong(children.get(3).text().replace(",", "")));
                        tranDetail.setChangedPrice(Double.parseDouble(children.get(4).text().replace(",", "")));
                        int hashCode = children.get(5).text().hashCode();
                        if (hashCode == 601827) {
                            tranDetail.setNature("B");
                        } else if (hashCode == 6020249) {
                            tranDetail.setNature("S");
                        } else {
                            tranDetail.setNature("M");
                        }
                        tranDetails.add(tranDetail);
                    }
                }

                try {
                    if (tbody.size() == 0) {
                        log.info("输入的代码有误或没有交易数据");
                        Thread.sleep(5000);
                        break;
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tranDetailMapper.batchInsert(tranDetails);
                tranDetails.clear();
            }

        }
    }
}
