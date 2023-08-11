package dzr.transaction.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import dzr.common.utils.DateUtils;
import dzr.common.utils.HttpClientService;
import dzr.common.utils.MathUtils;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * @Author dzr
 * @Date 2021/12/8 14:26
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionInitData  {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final TransactionMapper transactionMapper;

    /**
     * 2 后复权
     * 1 前复权
     * 0 不复权
     */
    private static final String[] FQT = {"0","1"};
    private static final String[] KLT = {"101","5","15","30","60"};

    private static final String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get?fields1=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13&fields2=f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61&beg=$begin&end=20500101&ut=fa5fd1943c7b386f172d6893dbfba10b&rtntype=6&secid=$code&klt=$klt&fqt=$fqt";


    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            log.info(securityCode.toString());
            ArrayList<Transaction> transactions =new ArrayList<>();
            HashMap<Date,Transaction> tranDayMap = new HashMap<>();
            Date reportDate = getNewestReportDate(securityCode);
            for (int k = 0; k < KLT.length; k++) {
                for (int j = 0; j < FQT.length; j++) {
                    String urlPre = url.replace("$code", securityCode.getCodeWithNumber()).replace("$fqt", FQT[j])
                            .replace("$klt", KLT[k]).replace("$begin", DateUtils.dateToStr(reportDate,"yyyyMMdd"));
                    String ret = httpClientService.doGet(urlPre);
                    if (ret.length() > 100) {

                        JSONArray data = JSON.parseObject(ret).getJSONObject("data").getJSONArray("klines");

                        for (int i = 0; i < data.size(); i++) {
                            try {
                                String[] one = data.getString(i).split(",");
                                String date = one[0].length() > 10 ? one[0] + ":00" : one[0] + " 00:00:00";
                                Transaction transaction = new Transaction();
                                transaction.setCode(securityCode.getCode());
                                transaction.setName(securityCode.getName());
                                transaction.setReportDate(DateUtils.strToDate(date));
                                transaction.setTopen(Double.valueOf(one[1]));
                                transaction.setTclose(Double.valueOf(one[2]));
                                transaction.setHigh(Double.valueOf(one[3]));
                                transaction.setLow(Double.valueOf(one[4]));

                                transaction.setVoturnover(Double.valueOf(one[5]));
                                transaction.setVaturnover(Double.valueOf(one[6]));

                                transaction.setAmplitude(Double.valueOf(one[7]));
                                transaction.setPchg(Double.valueOf(one[8]));
                                transaction.setChg(Double.valueOf(one[9]));
                                transaction.setTurnover(Double.valueOf(one[10]));
                                transaction.setEnergy(MathUtils.doubleRetain2Bit(transaction.getAmplitude() / transaction.getTurnover()));
                                transaction.setReinstatement(FQT[j]);
                                transaction.setDataType(KLT[k]);
                                tranDayMap.put(transaction.getReportDate(), transaction);
                                transactions.add(transaction);
                            } catch (Exception e) {
                                System.out.println(ret);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            transactionMapper.batchInsert(transactions);
        });

    }

    private Date getNewestReportDate(SecurityCode securityCode){
        Transaction transaction = transactionMapper.selectNewestByCode(securityCode.getCode());
        if (transaction == null || transaction.getReportDate() == null){
            return securityCode.getListingDate();
        }
        return transaction.getReportDate();
    }


}
