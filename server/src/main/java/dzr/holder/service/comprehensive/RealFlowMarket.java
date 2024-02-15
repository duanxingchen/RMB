package dzr.holder.service.comprehensive;

import com.google.common.util.concurrent.AtomicDouble;
import dzr.common.utils.MathUtils;
import dzr.holder.entity.HolderNum;
import dzr.holder.mapper.HolderNumMapper;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.TenFlowHolder;
import dzr.organization.mapper.TenFlowHolderMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


/**
 * 盘子小，实际流动盘小于30亿（流动盘中减去前十大流通股东后的市值）
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RealFlowMarket implements Filter{

    private final TenFlowHolderMapper tenFlowHolderMapper;
    private final TransactionMapper transactionMapper;
    private final HolderNumMapper holderNumMapper;
    private final static int REAL_MARKET = 30;
    private final static double MAKERS_COST_RATE = 0.3;

    /**
     * 过滤掉不符合的code，返回股票代码
     * @return
     */
    @Override
    public List<SecurityCode> filter(List<SecurityCode> securityCodeList){
        ArrayList<SecurityCode> includeSecurityCodes = new ArrayList<>();
        securityCodeList.forEach(securityCode -> {
            log.info("Shareholders : {}",securityCode);
            List<TenFlowHolder> tenFlowHolders = tenFlowHolderMapper.selectByCode(securityCode.getCode());
            List<Transaction> transactions = transactionMapper.selectReinstatementByCode(securityCode.getCode());
            List<HolderNum> holderNums =  holderNumMapper.selectByCode(securityCode.getCode());
            holderNums = holderNums.stream().sorted(Comparator.comparing(HolderNum::getReportDate).reversed()).collect(Collectors.toList());
            if (tenFlowHolders.size() > 0){
                tenFlowHolders.sort(Comparator.comparing(TenFlowHolder::getReportDate).reversed());
                TenFlowHolder newFlowHolders = tenFlowHolders.get(0);

                double rate =tenFlowHolders.stream()
                        .filter(tenFlowHolder -> tenFlowHolder.getReportDate().getTime() == newFlowHolders.getReportDate().getTime())
                        .map(tenFlowHolder -> tenFlowHolder.getFreeHoldnumRatio())
                        .mapToDouble(BigDecimal::doubleValue).sum();

                transactions = transactions.stream().sorted(Comparator.comparing(Transaction::getReportDate).reversed()).collect(Collectors.toList());

                double market = MathUtils.doubleRetain2Bit(transactions.get(0).getVaturnover()/transactions.get(0).getTurnover()/1000000);

                double makersCostRate = MathUtils.doubleRetain2Bit(transactions.get(0).getTclose()/getMakersCost(holderNums, transactions)- 1);
                if( (rate * market)/100 < REAL_MARKET  && makersCostRate < MAKERS_COST_RATE){
                    securityCode.setName(securityCode.getName() + "   真实流动市值(亿):" +MathUtils.doubleRetain2Bit((rate * market)/100)  + "   主力利润率(%)：" + makersCostRate*100);
                    includeSecurityCodes.add(securityCode);
                }
            }

        });
        return includeSecurityCodes;

    }


    private double getMakersCost(List<HolderNum> holderNums,List<Transaction> transactions) {
        HolderNum moreHolderNum = holderNums.get(0);
        int moreLocation = 0;

        for (int i = 0; i < holderNums.size() && i < 6; i++) {
            if (holderNums.get(i).getHolderNum() > moreHolderNum.getHolderNum()){
                moreHolderNum = holderNums.get(i);
                moreLocation = i;
            }
        }

        HashMap<Double, Long> map = new HashMap<>();
        long divSumHolderNum = 0;
        for (int j = 0; j < moreLocation; j++) {
            Date startDate = holderNums.get(j+1).getReportDate();
            Date endDate = holderNums.get(j).getReportDate();
            double vagPrice = getVagPrice(transactions,startDate,endDate);
            long divHolderNum = Math.abs(holderNums.get(j).getHolderNum() - holderNums.get(j+1).getHolderNum());
            divSumHolderNum = divSumHolderNum + divHolderNum;
            Long value = map.get(vagPrice);
            if (value != null){
                divHolderNum = divHolderNum + value;
            }
            map.put(vagPrice,divHolderNum);
        }

        if (divSumHolderNum > 0){
            AtomicDouble cost = new  AtomicDouble(0.0);
            long finalDivSumHolderNum = divSumHolderNum;
            map.forEach(new BiConsumer<Double, Long>() {
                @Override
                public void accept(Double price, Long sumHolderNum) {
                    cost.addAndGet(price*sumHolderNum.doubleValue()/finalDivSumHolderNum);
                }
            });
            return cost.get();
        }
        return Double.MIN_VALUE;
    }

    private double getVagPrice(List<Transaction> transactions,Date startDate, Date endDate) {
        HashMap<Double, Double> map = new HashMap<>();
        double sumVoTurnOver = 0.0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            if (transaction.getReportDate().getTime() > startDate.getTime()
                    && transaction.getReportDate().getTime() < endDate.getTime()){
                Double tClose = transaction.getTclose();
                Double voTurnOver = transaction.getVoturnover();
                sumVoTurnOver = sumVoTurnOver + voTurnOver;
                Double value = map.get(tClose);
                if (value != null){
                    voTurnOver = voTurnOver + value;
                }
                map.put(tClose,voTurnOver);
            }
        }

        AtomicDouble price = new AtomicDouble(0.0);

        double finalSumVoTurnOver = sumVoTurnOver;
        map.forEach(new BiConsumer<Double, Double>() {
            @Override
            public void accept(Double tClose, Double voTurnOver) {
                price.getAndAdd(tClose * (voTurnOver / finalSumVoTurnOver));
            }
        });
        return price.get();
    }
}
