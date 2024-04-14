package dzr.transaction.init;

import dzr.common.config.ThreadPools;
import dzr.common.utils.DateUtils;
import dzr.common.utils.HttpClientService;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.transaction.entity.TranDetail;
import dzr.transaction.mapper.TranDetailTXMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;


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
    private final TranDetailTXMapper tranDetailTXMapper;


    private static String url = "https://stock.gtimg.cn/data/index.php?appn=detail&action=data&c=$code&p=$page";

    public void remountPullDataFromWeb() {
        AtomicInteger num = new AtomicInteger();
        List<SecurityCode> securityCodes = securityCodeMapper.selectAll();
        securityCodes.forEach(securityCode -> {
            try{
                        ArrayList<TranDetail> tranDetails = new ArrayList<>();
                        Date nowDate = DateUtils.getNowShortDate();
                        if (DateUtils.getHours() < 9){
                            nowDate.setDate(nowDate.getDate() -1);
                        }
            securityCode.setCode("300818");
                        String page = "0";
                        log.info("thread ： {}  name : {}  : page = {}",
                                Thread.currentThread().getName(), securityCode.toString(),page);
                        while (true){
                            String urlPre = url.replace("$code", securityCode.getCodeWithExchange()).replace("$page",page);
                            log.info(urlPre);
                            HttpClientService httpClientService = new HttpClientService();
                            Thread.sleep(2000);
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

                            if (tranDetails.size() > 0){
                                tranDetailTXMapper.batchInsert(tranDetails);
                                tranDetails.clear();
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        num.getAndIncrement();
                    }

        });
        while (true){
            if (Math.abs(num.get() - securityCodes.size()) < 20){
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                break;
            }
        }
    }
}
