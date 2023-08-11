package dzr.transaction.service.impl;


import dzr.common.utils.DateUtils;
import dzr.common.utils.MathUtils;
import dzr.holder.entity.Holder;
import dzr.holder.mapper.HolderMapper;
import dzr.info.entity.SecurityCode;
import dzr.transaction.entity.Energy;
import dzr.transaction.entity.Slide;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.EnergyMapper;
import dzr.transaction.mapper.SlideMapper;
import dzr.transaction.mapper.TransactionMapper;
import dzr.transaction.service.EnergyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2023/3/22 15:18
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EnergyServiceImpl implements EnergyService {
    private final TransactionMapper transactionMapper;
    private final HolderMapper holderMapper;
    private final SlideMapper slideMapper;
    private final EnergyMapper energyMapper;


    @Override
    public void calculate(SecurityCode securityCode) {
        ArrayList<Energy> energyArrayList = new ArrayList<>();
        List<Transaction> transactions = transactionMapper.selectNotReinstatementByCode(securityCode.getCode());
        List<Transaction> collect = transactions.stream().filter(transaction -> Math.abs(transaction.getPchg()) < 2)
                .sorted(Comparator.comparing(Transaction::getReportDate).reversed())
                .collect(Collectors.toList());
        Holder holder = holderMapper.selectOneByCode(securityCode.getCode());
        List<Slide> slides = slideMapper.selectByCode(securityCode.getCode());
        if (DateUtils.getBeforeDate(100).getTime() > collect.get(0).getReportDate().getTime() || holder.getListingDate().getTime() > DateUtils.getBeforeDate(2*365).getTime()){
            return  ;
        }
        double tenFlowHolderRatio = (holder != null && holder.getTenFlowHolderRatio() != null &&  holder.getTenFlowHolderRatio() <100) ? holder.getTenFlowHolderRatio() :0;
        HashMap<Long,Date> slideReportDate = getOutSlideReportDate(slides);

        List<Transaction> collectOutSlideReportDate = collect.stream().filter(transaction -> {
            if(slideReportDate.get(transaction.getReportDate().getTime()) == null){
                return true;
            }
            return false;
        }).sorted(Comparator.comparing(Transaction::getReportDate).reversed())
                .collect(Collectors.toList());

        Energy energy = new Energy();
        energy.setCode(securityCode.getCode());
        energy.setName(securityCode.getName());

        energy.setEnergyRat3(getEnergyRat(collect,5,tenFlowHolderRatio));
        energy.setEnergyRat5(getEnergyRat(collect,7,tenFlowHolderRatio));
        energy.setEnergyRat10(getEnergyRat(collect,10,tenFlowHolderRatio));
        energy.setEnergyRat15(getEnergyRat(collect,15,tenFlowHolderRatio));
        energy.setEnergyRat20(getEnergyRat(collect,20,tenFlowHolderRatio));
        energy.setEnergyRat30(getEnergyRat(collect,30,tenFlowHolderRatio));
        energy.setEnergyRat40(getEnergyRat(collect,40,tenFlowHolderRatio));
        energy.setEnergyRat60(getEnergyRat(collect,60,tenFlowHolderRatio));
        energy.setEnergyRat100(getEnergyRat(collect,100,tenFlowHolderRatio));
        energy.setEnergyAvg30(getEnergyRat(collectOutSlideReportDate,30,tenFlowHolderRatio));
        energy.setEnergyAvg60(getEnergyRat(collectOutSlideReportDate,60,tenFlowHolderRatio));
        energy.setEnergyAvg100(getEnergyRat(collectOutSlideReportDate,100,tenFlowHolderRatio));
        energy.setEnergyAvg150(getEnergyRat(collectOutSlideReportDate,150,tenFlowHolderRatio));
        energy.setEnergyAvg200(getEnergyRat(collectOutSlideReportDate,200,tenFlowHolderRatio));

        double rateMax = getMaxAndAvg(3,energy.getEnergyRat3(),energy.getEnergyRat5(),energy.getEnergyRat10(),energy.getEnergyRat15(),energy.getEnergyRat20()
        ,energy.getEnergyRat30(),energy.getEnergyRat40(),energy.getEnergyRat60(),energy.getEnergyRat100());

        double rateMin = getMinAndAvg(3,energy.getEnergyAvg30(),energy.getEnergyAvg60(),energy.getEnergyAvg100(),energy.getEnergyAvg150()
                ,energy.getEnergyAvg200());

        energy.setRankNum(MathUtils.doubleRetain2Bit(rateMax/rateMin));
        energyArrayList.add(energy);
        energyMapper.batchInsert(energyArrayList);
    }

    private double getMaxAndAvg(int num,  Double ... rat) {
        double sum = 0;
        ArrayList<Double> ret = new ArrayList<>();
        for (int i = 0; i < rat.length; i++) {
            ret.add(rat[i]);
        }
        ret.sort(Comparator.comparing(Double::doubleValue).reversed());
        for (int i = 0; i < num && i < ret.size(); i++) {
            sum = sum + ret.get(i);
        }
        return MathUtils.doubleRetain2Bit(sum/num);
    }

    private double getMinAndAvg(int num,  Double ... rat) {
        double sum = 0;
        ArrayList<Double> ret = new ArrayList<>();
        for (int i = 0; i < rat.length; i++) {
            ret.add(rat[i]);
        }
        ret.sort(Comparator.comparing(Double::doubleValue));
        for (int i = 0; i < num && i < ret.size(); i++) {
            sum = sum + ret.get(i);
        }
        return MathUtils.doubleRetain2Bit(sum/num);
    }

    private HashMap<Long, Date> getOutSlideReportDate(List<Slide> slides) {
        HashMap<Long, Date> hashMap = new HashMap<>();
        slides.forEach(slide -> {
            List<Date> betweenDates = DateUtils.findBetweenDates(slide.getStartDate(), slide.getEndDate());
            betweenDates.forEach(date -> {
                hashMap.put(date.getTime(),date);
            });
        });

        return hashMap;
    }


    private Double getEnergyRat(List<Transaction> collect, int days,double tenFlowHolderRatio) {
        double sum = 0;
        for (int i = 0; i < collect.size() && i < days; i++) {
            sum = sum + collect.get(i).getEnergy();
        }
        double vv = Math.min(days,collect.size());
        return MathUtils.doubleRetain2Bit(sum/vv/(100/(100 - tenFlowHolderRatio)));
    }
}
