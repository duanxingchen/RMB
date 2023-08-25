package dzr.transaction.service.impl;


import com.google.common.util.concurrent.AtomicDouble;
import dzr.common.utils.MathUtils;
import dzr.holder.entity.Holder;
import dzr.holder.mapper.HolderMapper;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.transaction.entity.Cost;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.CostMapper;
import dzr.transaction.mapper.TransactionMapper;
import dzr.transaction.service.CostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CostServiceImpl implements CostService {
    private final SecurityCodeMapper securityCodeMapper;
    private final TransactionMapper transactionMapper;
    private final HolderMapper holderMapper;
    private final CostMapper costMapper;

    @Override
    public void calculate() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            log.info(securityCode.toString());
            Cost cost = new Cost();
            double sanHuRate;
            Holder holder = holderMapper.selectOneByCode(securityCode.getCode());
            if (holder != null) {
                double tenFlowHolderRatio = holder.getTenFlowHolderRatio() == null ? 0.0 : holder.getTenFlowHolderRatio();
                cost.setDongCaiIndustry(holder.getDongCaiIndustry());
                sanHuRate = 100 / (100 - tenFlowHolderRatio);
            } else {
                sanHuRate = 100.0;
            }
            List<Transaction> transactions = transactionMapper.selectReinstatementByCode(securityCode.getCode());
            transactions.sort((o1, o2) -> o1.getReportDate().before(o2.getReportDate()) ? 1 : -1);
            cost.setCode(securityCode.getCode());
            cost.setName(securityCode.getName());

            //cost.setDays1ZfRate(getZfRate(transactions,1));
            //cost.setDays1ZfRate(getZfRate(transactions,3));
            //cost.setDays1ZfRate(getZfRate(transactions,5));
            cost.setDays1HandsRate(getHandsRate(transactions, 1, sanHuRate));
            cost.setDays3HandsRate(getHandsRate(transactions, 3, sanHuRate));
            cost.setDays5HandsRate(getHandsRate(transactions, 5, sanHuRate));
            cost.setDays1PriceRate(getPriceRate(transactions, 1));
            cost.setDays3PriceRate(getPriceRate(transactions, 3));
            cost.setDays5PriceRate(getPriceRate(transactions, 5));
            cost.setDays10PriceRate(getPriceRate(transactions, 10));
            cost.setDays30PriceRate(getPriceRate(transactions, 30));
            costMapper.delete(cost);
            costMapper.insert(cost);
        });
    }


    private Double getZfRate(List<Transaction> transactions, int days) {
        double price = 0.0;
        if (transactions.size() > days){
            price = 100*(transactions.get(0).getTclose() -  transactions.get(days).getTclose())/transactions.get(days).getTclose();
        }
        return MathUtils.doubleRetain2Bit(price);
    }


    private Double getPriceRate(List<Transaction> transactions, int days) {
        double price = 0.0;
        if (transactions.size() > days){
            price = 100*(transactions.get(0).getTclose() -  transactions.get(days).getTclose())/transactions.get(days).getTclose();
        }
        return MathUtils.doubleRetain2Bit(price);
    }

    private Double getHandsRate(List<Transaction> transactions, int days,double sanHuRate) {
        double sumHandsRate = 0.0;
        for (int i = 0; i < days && i < transactions.size(); i++) {
            sumHandsRate = sumHandsRate + transactions.get(i).getTurnover();
        }
        return MathUtils.doubleRetain2Bit(sumHandsRate*sanHuRate);
    }

    private Double getCostPrice(List<Transaction> transactions, int days) {
        double sumVaturnover =0.0;
        HashMap<Long, Transaction> map = new HashMap<>();
        for (int i = 0; i < days  && i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            map.put(transaction.getReportDate().getTime(), transaction);
            sumVaturnover = sumVaturnover + transaction.getVaturnover();
        }

        AtomicDouble price = new AtomicDouble(0.0);

        double finalSumVaturnover = sumVaturnover;
        map.forEach((reportDate, transaction) -> price.getAndAdd(transaction.getTclose() * (transaction.getVoturnover() / finalSumVaturnover)));
        return MathUtils.doubleRetain2Bit(price.get());
    }
}
