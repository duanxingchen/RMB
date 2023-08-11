package dzr.integration.service.impl;

import dzr.common.config.ThreadPools;
import dzr.common.utils.DateUtils;
import dzr.common.utils.MathUtils;
import dzr.info.entity.SecurityCode;
import dzr.integration.service.SimilarityService;
import dzr.integration.service.dto.SimilaritySave;
import dzr.integration.service.dto.SquareRoot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @Author dzr
 * @Date 2023/4/7 9:50
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
public class SimilarityServiceImpl implements SimilarityService {

    @Override
    public CompletableFuture<List<SquareRoot>> calculate(SecurityCode smallSecurityCode,
                                                         SecurityCode bigSecurityCode,
                                                         List<Double> smallFutureData,
                                                         List<Double> smallData,
                                                         int days,
                                                         int futureDays) {

        return CompletableFuture.supplyAsync(()->{
            long start = System.currentTimeMillis();
            List<SimilaritySave> similaritySaves = null;
            /*List<SimilaritySave> similaritySaves = similarityRedis.getSimilaritySave(bigSecurityCode.getCode());*/
            long end = System.currentTimeMillis();
            //log.info("read data : {}   time: {} ms", bigSecurityCode.getName(),(end-start));



            long start2 = System.currentTimeMillis();


            /**
             * 移除相同股票，待比对的数据
             */
            if (smallSecurityCode.getCode().equals(bigSecurityCode.getCode())){
                for (int i = 0; i < days; i++) {
                    similaritySaves.remove(0);
                }
            }

            HashMap<Date, ArrayList<Double>> bigDataMap = new HashMap<>();
            HashMap<Date, ArrayList<Double>> bigFutureDataMap = new HashMap<>();

            for (int step = futureDays; step < similaritySaves.size() - days; step++) {
                ArrayList<Double> one = new ArrayList<>();
                ArrayList<Double> oneFuture = new ArrayList<>();
                for (int i = 0; i < days; i++) {
                    one.add(similaritySaves.get(step + i).getTclose());
                }
                for (int i = futureDays; i >= 0; i--) {
                    oneFuture.add(similaritySaves.get(step - i).getTclose());
                }
                bigFutureDataMap.put(similaritySaves.get(step).getReportDate(),oneFuture);
                bigDataMap.put(similaritySaves.get(step).getReportDate(),one);
            }

            ArrayList<SquareRoot> squareRoots = new ArrayList<>();

            bigDataMap.forEach((date, doubles) -> {
                SquareRoot squareRoot = new SquareRoot();
                Collections.reverse(smallFutureData);
                ArrayList<Double> doubleArrayList = bigFutureDataMap.get(date);
                Collections.reverse(doubleArrayList);
                Double sqrtFuture = squareRoot(smallFutureData, doubleArrayList);
                squareRoot.setFuturePrice1(getFuturePriceRate(doubleArrayList,1));
                squareRoot.setFuturePrice3(getFuturePriceRate(doubleArrayList,3));
                squareRoot.setFuturePrice5(getFuturePriceRate(doubleArrayList,5));
                squareRoot.setFuturePrice10(getFuturePriceRate(doubleArrayList,10));
                Double sqrt = squareRoot(smallData,doubles);
                squareRoot.setCode(smallSecurityCode.getCode());
                squareRoot.setName(smallSecurityCode.getName());
                squareRoot.setVsCode(bigSecurityCode.getCode());
                squareRoot.setVsName(bigSecurityCode.getName());
                squareRoot.setSqrt(sqrt);
                squareRoot.setSqrtFuture(sqrtFuture);
                squareRoot.setVsStartDate(date);
                squareRoots.add(squareRoot);
                clearSquareRoots(squareRoots,days);
            });

            long end2 = System.currentTimeMillis();
            //log.info("calculate : {}   time: {} ms", bigSecurityCode.getName(),(end2-start2));

            return squareRoots;
        }, ThreadPools.getExecutor());

    }

    private Double getFuturePriceRate(ArrayList<Double> doubles, int days) {
        return MathUtils.doubleRetain2Bit((doubles.get(days) - doubles.get(0))*100 / doubles.get(0));
    }

    private void clearSquareRoots(ArrayList<SquareRoot> squareRoots, int days) {
        squareRoots.sort(Comparator.comparing(SquareRoot::getVsStartDate));
        for (int i = 0; i < squareRoots.size() -1; i++) {
            if (DateUtils.differentDays(squareRoots.get(i).getVsStartDate(), squareRoots.get(i+1).getVsStartDate()) < days){
                if(squareRoots.get(i).getSqrt()> squareRoots.get(i+1).getSqrt()){
                    squareRoots.remove(i);
                }else {
                    squareRoots.remove(i+1);
                }
                i--;
            }
        }

        squareRoots.sort(Comparator.comparing(SquareRoot::getSqrt));

        for (int i = 2; i < squareRoots.size(); i++) {
            squareRoots.remove(2);
        }
    }

    /**
     * 平方根
     * @param smallData
     * @param doubles
     * @return
     */
    private Double squareRoot(List<Double> smallData, ArrayList<Double> doubles) {
        int size = smallData.size();
        if (size != doubles.size()){
            log.warn("数据大小不匹配");
            return Double.MAX_VALUE;
        }

        ArrayList<Double> multiples = new ArrayList<>(size);

        /**
         * 同比变化倍数
         */
        for (int i = 0; i < size; i++) {
            double multiple = smallData.get(i)/doubles.get(i);
            if (multiple < 1.0){
                multiple = 1/multiple;
            }
            multiples.add(multiple);
        }
        /**
         * 均方差
         */
        double avg = multiples.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
        double sqrt = Math.sqrt(multiples.stream().map(d -> {
            return Math.pow(d - avg, 2);
        }).mapToDouble(Double::doubleValue).average().getAsDouble());
        return sqrt;
    }
}
