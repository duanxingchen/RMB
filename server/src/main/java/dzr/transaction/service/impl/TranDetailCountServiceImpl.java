package dzr.transaction.service.impl;

import dzr.common.utils.MathUtils;
import dzr.holder.entity.Holder;
import dzr.info.entity.SecurityCode;
import dzr.transaction.entity.TranDetail;
import dzr.transaction.entity.TranDetailCount;
import dzr.transaction.entity.Transaction;
import dzr.transaction.service.TranDetailCountService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author dzr
 * @Date 2023/3/20 22:06
 * @Version 1.0
 * @Description:
 */
@Service
public class TranDetailCountServiceImpl implements TranDetailCountService {
    /**
     * 分钟
     */
    private static final double TIME = 1;
    /**
     * 斜率
     */
    private static final double XIE_LU_RATE = 1;

    /**
     * 价格
     */
    private static final double PRICE_RATE = 1;

    @Override
    public List<TranDetailCount> calculate(SecurityCode securityCode, List<TranDetail> tranDetails, List<Transaction> transactions, Holder holder) {
        ArrayList<TranDetailCount> tranDetailCounts = new ArrayList<>();

        Map<Date, List<TranDetail>> map = tranDetails.stream().collect(Collectors.groupingBy(TranDetail::getReportDate));
        map.forEach((date, tranDetailList) -> {
            tranDetailList.sort(Comparator.comparing(TranDetail::getTranTime));
            double upNum = 0.0;
            double downNum = 0.0;
            double priceUpSum = 0.0;
            double priceDownSum = 0.0;
            double handUpSum = 0.0;
            double handDownSum = 0.0;
            for (int i = 0; i < tranDetailList.size(); i++) {
                TranDetail start = tranDetailList.get(i);
                double priceSum = 0.0;
                double handSum = 0.0;


                for (int j = i+1; j < tranDetailList.size(); j++) {
                    TranDetail end = tranDetailList.get(j);
                    priceSum = priceSum + end.getChangedPrice();
                    handSum = handSum + end.getChangedHands();

                    /**
                     * 时间差分钟
                     */
                    double difTime = (end.getTranTime().getTime() - start.getTranTime().getTime())/1000.0/60.0;
                    double difPrice = end.getPrice() - start.getPrice();
                    /**
                     * 价格差
                     */
                    double rate = 100*difPrice / start.getPrice();
                    /**
                     * 斜率,如果为1，表示一分钟内，价格上涨（下跌） 1%
                     */
                    double xieLu = rate/difTime;
                    /**
                     * 符合价格变动大于1，同时在一分钟时间内，进行统计
                     */
                    if ( Math.abs(rate) > PRICE_RATE && Math.abs(xieLu) > XIE_LU_RATE){
                        if (rate > 0){
                            upNum  = upNum + rate;
                            priceUpSum = priceUpSum + priceSum ;
                            handUpSum = handUpSum + handSum;
                        }else {
                            downNum = downNum + Math.abs(rate);
                            priceDownSum = priceDownSum + priceSum ;
                            handDownSum = handDownSum + handSum;
                        }
                        i = j+1;
                        break;
                    }

                    /**
                     * 超过窗口时间，跳出
                     */
                    if(difTime > TIME){
                        break;
                    }
                }
            }

            if ( upNum > 0 || downNum >0 ){
                TranDetailCount tranDetailCount = new TranDetailCount();
                tranDetailCount.setName(securityCode.getName());
                tranDetailCount.setCode(securityCode.getCode());
                tranDetailCount.setReportDate(date);
                tranDetailCount.setUpNum(MathUtils.doubleRetain2Bit(upNum));
                tranDetailCount.setDownNum(MathUtils.doubleRetain2Bit(downNum));
                tranDetailCount.setPriceDownSum(MathUtils.doubleRetain2Bit(priceDownSum/10000));
                tranDetailCount.setPriceUpSum(MathUtils.doubleRetain2Bit(priceUpSum/10000));
                tranDetailCount.setHandDownSum(MathUtils.doubleRetain2Bit(handDownSum));
                tranDetailCount.setHandUpSum(MathUtils.doubleRetain2Bit(handUpSum));
                tranDetailCounts.add(tranDetailCount);
            }
        });
        return tranDetailCounts;
    }
}
