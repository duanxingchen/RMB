package dzr.transaction.service.impl;


import dzr.common.utils.DateUtils;
import dzr.info.entity.SecurityCode;
import dzr.transaction.entity.Slide;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.SlideMapper;
import dzr.transaction.mapper.TransactionMapper;
import dzr.transaction.service.SlideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2022/10/26 14:38
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SlideServiceImpl implements SlideService {
    private final TransactionMapper transactionMapper;
    private final SlideMapper slideMapper;

    private final static String UP = "上涨";
    private final static String DOWN = "下跌";
    private final static String INFLECTION_POINT = "拐点";
    private final static String HORIZONTAL_PLATE = "横盘";

    private double boTelThreshold = 1;
    private int numThreshold = 1000;
    private int RangeThreshold = 5;


    @Override
    public void calculate(SecurityCode securityCode) {
        log.info(securityCode.toString());
        List<Transaction> transactions = transactionMapper.selectReinstatementByCodeAndReportDates(securityCode.getCode(),DateUtils.strToDate("2020-01-01 00:00:00"));
        transactions.sort(Comparator.comparing(Transaction::getReportDate));
        List<Slide> slides = selfAdaption(securityCode,transactions,numThreshold, RangeThreshold);
        if (slides.size() > 0){
            slideMapper.batchDelete(slides);
            slideMapper.batchInsert(slides);
        }
    }

    private List<Slide> selfAdaption(SecurityCode securityCode,List<Transaction> transactions,int numThreshold,int RangeThreshold) {
        int maxStep = 60;
        int maxPoll = 100;
        int beforeSize = 0;
        Slide beforeSlide = new Slide();
        List<Slide> accurates = new ArrayList<>();
        for (int step = 3; step < maxStep; step++) {
            for (int poll = 1; poll <maxPoll; poll++) {
                ArrayList<Slide> accurate = getSlides(securityCode,transactions,step, poll);
                if (accurate.size() == 0){
                    break;
                }
                if (beforeSize == accurate.size() &&
                        (beforeSlide.getRange()== null?0.0:beforeSlide.getRange())==accurate.get(accurate.size()-1).getRange())
                {
                    break;
                }
                beforeSize = accurate.size();
                beforeSlide = accurate.get(accurate.size()-1);
                accurates.addAll(accurate);
            }
            if ( (accurates.size()>100) || ((step == (maxStep -1)) && (accurates.size()>2))){
                accurates = distinct(transactions,accurates,numThreshold,RangeThreshold);
            }
        }
        return accurates;
    }

    /**
     * 去掉同时间段，两个时间段的交集
     * @param accurates
     * @return
     */
    private List<Slide> distinct(List<Transaction> transactions,List<Slide> accurates,int numThreshold,int RangeThreshold) {
        List<Slide> slides = new ArrayList<>();
        List<Slide> slidesRet = new ArrayList<>();
        HashMap<String, Slide> map = new HashMap<>();
        HashMap<String, Slide> mapIntersection = new HashMap<>();
        accurates.forEach(acc->map.put(String.valueOf(acc.getStartDate().getTime())+String.valueOf(acc.getEndDate().getTime()),acc));
        map.entrySet().forEach(stringSlideEntry -> {
            Slide value = stringSlideEntry.getValue();
            if (value.getRange() > RangeThreshold) {
                slides.add(value);
            }
        });

        if (slides.size()<2){
            return slides;
        }


        slides.sort((o1, o2) -> o2.getRange().compareTo(o1.getRange()));

        for (int i = 0; i < slides.size(); i++) {
            Slide out = slides.get(i);
            for (int j = i+1; j < slides.size(); j++) {
                Slide in = slides.get(j);
                if(intersection(transactions,in, out) > 0.5){
                    mapIntersection.put(String.valueOf(in.getStartDate().getTime())+String.valueOf(in.getEndDate().getTime()),in);
                }
            }
        }
        map.entrySet().forEach(stringSlideEntry -> {
            if (mapIntersection.get(stringSlideEntry.getKey())==null){
                slidesRet.add(stringSlideEntry.getValue());
            }
        });
        slidesRet.sort((o1, o2) -> o2.getRange().compareTo(o1.getRange()));


        return slidesRet.stream().filter(slide -> slide.getRange()>RangeThreshold).limit(numThreshold).collect(Collectors.toList());
    }

    private double intersection(List<Transaction> transactions,Slide in, Slide out) {
        if (in.getEndDate().before(out.getStartDate()) || in.getStartDate().after(out.getEndDate())){
            return 0.0;
        }
        List<Transaction> inDays = transactions.stream().filter(tran -> {
            Date reportDate = tran.getReportDate();
            if (in.getStartDate().before(reportDate) && in.getEndDate().after(reportDate)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        List<Transaction> outDays = transactions.stream().filter(tran -> {
            Date reportDate = tran.getReportDate();
            if (out.getStartDate().before(reportDate) && out.getEndDate().after(reportDate)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        double num =0;
        for (int i = 0; i < outDays.size(); i++) {
            for (int j = 0; j < inDays.size(); j++) {
                if (inDays.get(j).getReportDate().getTime() == outDays.get(i).getReportDate().getTime()){
                    num++;
                }
            }
        }
        return num+2/outDays.size()+2;
    }


    private ArrayList<Slide> getSlides(SecurityCode securityCode,List<Transaction> transactions, int step, int poll) {
        ArrayList<Transaction> ave = slidingAverageCalculate(transactions,step,poll);
        ArrayList<Slide> statistics = statistics(securityCode,ave, step,poll);
        return accurateTimeCalculation(transactions,statistics,step,poll);
    }

    private ArrayList<Slide> accurateTimeCalculation(List<Transaction> transactions,ArrayList<Slide> slides, int step, int pool) {
        ArrayList<Slide> slidesRet = new ArrayList<>();
        step = Math.min(step * pool, 40);

        for (int i = 0; i < slides.size(); i++) {
            Slide slide = slides.get(i);
            Date startDate = slide.getStartDate();
            Date endDate = slide.getEndDate();
            double startValue = 0.0;
            double endValue = 0.0;

            for (int j = 0; j < transactions.size(); j++) {
                long time = transactions.get(j).getReportDate().getTime();
                if (time == startDate.getTime()){
                    startValue =transactions.get(j).getTclose();
                    for (int k = -(step)/2; k < step/2; k++) {
                        if ( 0 < j+k  && j+k < transactions.size() ){
                            Transaction transaction = transactions.get(j + k);

                            if (slide.getType().equals(UP) &&  transaction.getTclose() < startValue){
                                startValue = transaction.getTclose();
                                slide.setStartDate(transaction.getReportDate());
                            }
                            if (slide.getType().equals(DOWN) &&  transaction.getTclose() > startValue){
                                startValue = transaction.getTclose();
                                slide.setStartDate(transaction.getReportDate());
                            }
                        }

                    }
                }

                if (time == endDate.getTime()){
                    endValue =transactions.get(j).getTclose();
                    /**
                     * 经过层层向上抽取，结束的时候已经抽取少了pool*(step/2),所以最后，新的数据已经无法取到
                     * 需要手动补齐
                     */
                    int addDays = 0;
                    if(DateUtils.differentDays(endDate,transactions.get(transactions.size()-1).getReportDate()) < pool*(step/2))
                    {
                        addDays = pool*(step/2);
                    }
                    for (int k = -step/2; k < step/2 + addDays; k++) {
                        if (0< j+k  && j+k < transactions.size()){
                            Transaction transaction = transactions.get(j + k);
                            if (slide.getType().equals(UP) && transaction.getTclose() > endValue){
                                endValue = transaction.getTclose();
                                slide.setRange((endValue-startValue)*100/startValue);
                                slide.setEndDate(transaction.getReportDate());
                            }
                            if (slide.getType().equals(DOWN) && transaction.getTclose() < endValue){
                                endValue = transaction.getTclose();
                                slide.setRange((startValue-endValue)*100/endValue);
                                slide.setEndDate(transaction.getReportDate());
                            }
                        }
                    }
                }

            }
            int days = (int)transactions.stream().filter(tran -> slide.getStartDate().before(tran.getReportDate()) &&
                    slide.getEndDate().after(tran.getReportDate())).count();
            if (days > 0 && !slide.getRange().isInfinite()){
                slide.setDays(days+2);
                slidesRet.add(slide);
            }
        }
        return slidesRet;
    }

    private ArrayList<Slide> statistics(SecurityCode securityCode,ArrayList<Transaction> trans, int step, int poll) {
        ArrayList<Slide> slides = new ArrayList<>();
        for (int i = 0; i < trans.size(); i++) {
            Transaction transaction = trans.get(i);
            String name = transaction.getName();
            if (name == null){
                continue;
            }
            if (name.equals(UP)|| name.equals(DOWN)){
                Slide slide = new Slide();
                slide.setCode(securityCode.getCode());
                slide.setName(securityCode.getName());
                slide.setStep(step);
                slide.setPoll(poll);
                slide.setStartDate(transaction.getReportDate());
                int j = 0;
                while (true){
                    if ( trans.get(i+j).getName().equals(name)){
                        if(i+j == trans.size()-1 ){
                            slide.setRange((trans.get(i+j).getTclose()-trans.get(i).getTclose())*100/trans.get(i).getTclose());
                            slide.setEndDate(trans.get(i+j).getReportDate());
                            slide.setType(name);
                            i = i+j+1;
                            break;
                        }
                        ++j;
                    }else{
                        slide.setRange((trans.get(i+j).getTclose()-trans.get(i).getTclose())*100/trans.get(i).getTclose());
                        slide.setEndDate(trans.get(i+j).getReportDate());
                        slide.setType(name);
                        i = i+j+1;
                        break;
                    }
                }
                slides.add(slide);
            }
        }

        return slides;

    }

    private ArrayList<Transaction> slidingAverageCalculate( List<Transaction> transactions,int step,int poll) {
        ArrayList<Transaction> avgTransactions = new ArrayList<>();
        for (int i = 0; i < transactions.size()-step; i++) {
            double priceSum = 0.0;
            for (int j = 0; j < step; j++) {
                priceSum = priceSum + transactions.get(i+j).getTclose();
            }
            Transaction tran = new Transaction();
            tran.setReportDate(transactions.get(i+step/2).getReportDate());
            tran.setTclose(priceSum/step);
            avgTransactions.add(tran);
        }

        for (int i = 0; i < avgTransactions.size()-2; i++) {
            double s1 = avgTransactions.get(i+1).getTclose() - avgTransactions.get(i).getTclose();
            double s2 = avgTransactions.get(i+2).getTclose() - avgTransactions.get(i+1).getTclose();
            double boTel21 = Math.abs((avgTransactions.get(i+2).getTclose() -avgTransactions.get(i+1).getTclose())/ avgTransactions.get(i+1).getTclose()) * 100;
            double boTel20 = Math.abs((avgTransactions.get(i+2).getTclose() -avgTransactions.get(i).getTclose())/ avgTransactions.get(i+1).getTclose()) * 100;

            if ((s1 > 0 && s2 > 0) && boTel21> boTelThreshold && boTel20> boTelThreshold){
                avgTransactions.get(i+2).setName(UP);
            }else if((s1 < 0 && s2 < 0) && boTel21>boTelThreshold && boTel20> boTelThreshold){
                avgTransactions.get(i+2).setName(DOWN);
            }else if(((s1 < 0 && s2 > 0) || (s1 > 0 && s2 < 0)) && boTel21>boTelThreshold && boTel20> boTelThreshold){
                avgTransactions.get(i+2).setName(INFLECTION_POINT);
            }else {
                avgTransactions.get(i+2).setName(HORIZONTAL_PLATE);
            }
        }
        if (poll-- >0){
            avgTransactions = slidingAverageCalculate(avgTransactions, step, poll);
        }

        return avgTransactions;
    }

}
