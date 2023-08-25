package dzr.transaction.init;

import dzr.common.utils.DateUtils;
import dzr.common.utils.HttpClientService;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.transaction.entity.TranDetail;
import dzr.transaction.mapper.TranDetailMapper;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


/**
 * @Author dzr
 * @Date 2022/9/19 15:00
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TranDetailInitData  {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final TranDetailMapper tranDetailMapper;


    private static String url = "https://stock.gtimg.cn/data/index.php?appn=detail&action=data&c=$code&p=$page";

    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            ArrayList<TranDetail> tranDetails = new ArrayList<>();

            Date nowDate = DateUtils.getNowShortDate();
            String page = "0";
            log.info("thread ： {}  name : {}  : page = {}",
                    Thread.currentThread().getName(), securityCode.getName(),page);
            while (true){
                String urlPre = url.replace("$code", securityCode.getCodeWithExchange()).replace("$page",page);
                log.debug(urlPre);
                String[] ret = httpClientService.doGet(urlPre).split(",");
                if(ret.length < 2){
                    break;
                }
                page = String.valueOf(Integer.parseInt(ret[0].split("\\[")[1]) + 1);
                String[] data = ret[1].substring(1,ret[1].length()-2).split("\\|");
                Arrays.stream(data).forEach(one->{
                    TranDetail tranDetail = new TranDetail();
                    String[] splitOne = one.split("/");
                    tranDetail.setCode(securityCode.getCode());
                    tranDetail.setReportDate(nowDate);
                    tranDetail.setTranTime(DateUtils.strToDate(splitOne[1],"HH:mm:ss"));
                    tranDetail.setPrice(Double.parseDouble(splitOne[2]));
                    tranDetail.setChangePrice(Double.parseDouble(splitOne[3]));
                    tranDetail.setChangedHands(Long.parseLong(splitOne[4]));
                    tranDetail.setChangedPrice(Double.parseDouble(splitOne[5]));
                    if (splitOne[6].equals("B")){
                        tranDetail.setNature("M");
                    }else {
                        tranDetail.setNature(splitOne[6]);
                    }
                    tranDetails.add(tranDetail);
                });
            }
            tranDetailMapper.batchInsert(tranDetails);
        });
    }
}
