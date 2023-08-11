package dzr.transaction.service.impl;

import dzr.common.utils.DateUtils;
import dzr.info.entity.SecurityCode;
import dzr.plate.entity.PlateStock;
import dzr.transaction.entity.Slide;
import dzr.transaction.entity.Transaction;
import dzr.transaction.service.EnergyPlateCountService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2022/10/26 14:38
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
public class EnergyPlateCountServiceImpl implements EnergyPlateCountService {

    /**
     * 初始化等级划分为100
     */
    private static final int DEF_INIT = 100;
    private static final double DEF_MIN_AVG_RATE = 0.8;
    /**
     * setp 为偶数
     */
    private static final  int[] DEPTH_STEP = {1,2,3,4,5,6,7};

    private static final  double ENERGY_RANG = 0.5;

    private static final  int ENERGY_TIMES = 33;


    private static final  int NUM_DAYS = 10;

    private SecurityCode securityCode;
    Map<String, List<Transaction>> dataTypeMap;
    private List<Slide> slides;



    @Override
    public List<Transaction> calculate(SecurityCode securityCode, List<Transaction> transactions, List<Slide> slides, List<PlateStock> plates) {
        this.securityCode = securityCode;
        this.slides = slides;
        List<Transaction> tranRet = new ArrayList<>();
        dataTypeMap = transactions.stream().filter(transaction -> transaction.getReinstatement().equals("0"))
                .collect(Collectors.groupingBy(Transaction::getDataType));

        dataTypeMap.forEach(new BiConsumer<String, List<Transaction>>() {
            @Override
            public void accept(String dataType, List<Transaction> transactions) {
                log.info("{}  {} ",dataType,securityCode.toString());
                for (int i = 0; i < transactions.size(); i++) {
                    Transaction transaction = transactions.get(i);
                    transaction.setPlateCount(null);
                    transaction.setPlateUpCount(null);
                    transaction.setT3(null);
                    transaction.setAvgEnergy(null);
                    transaction.setNorTclose(null);
                    transaction.setBei(null);
                    transaction.setEnergyDo(null);
                    transaction.setPriceSection(null);
                    transaction.setSection(null);
                }

                HashMap<Double,Integer> gradeMap = new HashMap<>();
                /**
                 * 初始化数据，根据原始能动值，划分为100个等级的中位数值。
                 */
                transactions = initData(dataType,transactions,gradeMap);

                /**
                 * 循环处理，直到满足，最小相邻的聚类平均值变动比小于设定30%
                 */
                while(true){

                    /**
                     * 最小相邻的聚类平均值合并
                     */
                    if(union(gradeMap,transactions)){
                        break;
                    }

                    /**
                     * 根据时间排序，修补，修补条件为四提一，并且等级相差值小于设定值
                     */
                    repair(gradeMap,transactions);

                }


                /**
                 * 找出前N的最大波动时间段
                 */
                List<MaxEnergy> maxEnergies =  findMaxEnergyTimes(transactions);

                for (int i = 0; i <maxEnergies.size(); i++) {
                    MaxEnergy maxEnergy = maxEnergies.get(i);
                    for (int j = i; j < transactions.size(); j++) {
                        Transaction transaction = transactions.get(j);
                        if (transaction.getReportDate().before(maxEnergy.getEndDate()) &&
                                transaction.getReportDate().after(maxEnergy.getStartDate())){
                            transaction.setT3(maxEnergy.getAvgEnergy());
                        }
                    }
                }

                /**
                 * 计算异常次数
                 */
                addCountPlate(dataType,transactions,plates);
                tranRet.addAll(transactions);
            }
        });

        return tranRet;
    }

    private void addCountPlate(String dataType, List<Transaction> transactions, List<PlateStock> plates) {
        HashMap<Long, Transaction> hashMap = new HashMap<>();
        transactions.forEach(transaction -> hashMap.put(transaction.getReportDate().getTime(),transaction));
        Map<Date, List<PlateStock>> map = null;

        String up = "火箭发射";
        if (dataType.equals("101")){
            map = plates.stream().map(plateStock -> {
                PlateStock plate = new PlateStock();
                plate.setPlate(plateStock.getPlate());
                plate.setReportDate(DateUtils.dateToDate(plateStock.getReportDate(),"yyyy-MM-dd"));
                return plate;
            }).collect(Collectors.groupingBy(PlateStock::getReportDate));
        }

        if (dataType.equals("5")){
            map = plates.stream().map(plateStock -> {
                Date reportDate = plateStock.getReportDate();
                int minutes = DateUtils.getMinutes(reportDate);
                PlateStock plate = new PlateStock();
                plate.setPlate(plateStock.getPlate());
                reportDate.setSeconds(0);
                if(minutes >= 0 && minutes <= 5){
                    reportDate.setMinutes(5);
                }else if(minutes > 5 && minutes <= 10){
                    reportDate.setMinutes(10);
                }else if(minutes > 10 && minutes <= 15){
                    reportDate.setMinutes(15);
                }else if(minutes > 15 && minutes <= 20){
                    reportDate.setMinutes(20);
                }else if(minutes > 20 && minutes <= 25){
                    reportDate.setMinutes(25);
                }else if(minutes > 25 && minutes <= 30){
                    reportDate.setMinutes(30);
                }else if(minutes > 30 && minutes <= 35){
                    reportDate.setMinutes(35);
                }else if(minutes > 35 && minutes <= 40){
                    reportDate.setMinutes(40);
                }else if(minutes > 40 && minutes <= 45){
                    reportDate.setMinutes(45);
                }else if(minutes > 45 && minutes <= 50){
                    reportDate.setMinutes(50);
                }else if(minutes > 50 && minutes <= 55){
                    reportDate.setMinutes(55);
                }else if(minutes > 55 && minutes <= 60){
                    reportDate.setMinutes(60);
                }
                plate.setReportDate(reportDate);
                return plate;
            }).collect(Collectors.groupingBy(PlateStock::getReportDate));
        }

        if (dataType.equals("15")){
             map = plates.stream().map(plateStock -> {
                Date reportDate = plateStock.getReportDate();
                int minutes = DateUtils.getMinutes(reportDate);
                PlateStock plate = new PlateStock();
                plate.setPlate(plateStock.getPlate());
                reportDate.setSeconds(0);
                if(minutes >= 0 && minutes <= 15){
                    reportDate.setMinutes(15);
                }else if(minutes > 15 && minutes <= 30){
                    reportDate.setMinutes(30);
                }else if(minutes > 30 && minutes <= 45){
                    reportDate.setMinutes(45);
                }else if(minutes > 45 && minutes <= 60){
                    reportDate.setMinutes(60);
                }
                plate.setReportDate(reportDate);
                return plate;
            }).collect(Collectors.groupingBy(PlateStock::getReportDate));
        }

        if (dataType.equals("30")){
            map = plates.stream().map(plateStock -> {
                Date reportDate = plateStock.getReportDate();
                int minutes = DateUtils.getMinutes(reportDate);
                PlateStock plate = new PlateStock();
                plate.setPlate(plateStock.getPlate());
                reportDate.setSeconds(0);
                if(minutes >= 0 && minutes <= 30){
                    reportDate.setMinutes(30);
                }else if(minutes > 30 && minutes <= 60) {
                    reportDate.setMinutes(60);
                }
                plate.setReportDate(reportDate);
                return plate;
            }).collect(Collectors.groupingBy(PlateStock::getReportDate));
        }

        if (dataType.equals("60")){
             map = plates.stream().map(plateStock -> {
                Date reportDate = plateStock.getReportDate();
                int minutes = DateUtils.getMinutes(reportDate);
                PlateStock plate = new PlateStock();
                plate.setPlate(plateStock.getPlate());
                reportDate.setSeconds(0);
                if(minutes >= 0 && minutes <= 60){
                    reportDate.setMinutes(60);
                }
                plate.setReportDate(reportDate);
                return plate;
            }).collect(Collectors.groupingBy(PlateStock::getReportDate));
        }

        map.forEach((date,plateIn)->{
            Transaction transaction = hashMap.get(date.getTime());
            if (transaction != null){
                transaction.setPlateCount(plateIn.size());
                int upPlate = 0;
                for (int i = 0; i < plateIn.size(); i++) {
                    if (up.contains(plateIn.get(i).getPlate())) {
                        upPlate++;
                    }
                }
                transaction.setPlateUpCount(upPlate);
            }

        });

    }

    private List<MaxEnergy> findMaxEnergyTimes(List<Transaction> transactions) {

        /**
         * 根据时间进行排序编号,将时间编号装换为数字序号,将能动换成为倍数比例
         */
        initFindMaxTranDates(transactions);
        /**
         * 找出前N个最大的能动范围
         */

        List<MaxEnergy>  maxEnergies = findTopNMaxEnergy(transactions);

        return maxEnergies;
    }



    private List<MaxEnergy> findTopNMaxEnergy(List<Transaction> transactions) {
        /**
         * 找出连续区间，并统计能动次数
         */

        List<MaxEnergy> maxEnergies = findContinuationSection(transactions);


        /**
         * 合并统计区间，相邻区间平均能动最小的进行合并
         */
        while (true){
            if (maxEnergies.size() < 2){
                break;
            }
            maxEnergies = merge(maxEnergies);

            double minDays = maxEnergies.stream().filter(maxEnergy -> maxEnergy.getAdjacentEnergyRat()!= null).mapToDouble(m->{return m.getEndNum()-m.getStartNum();}).min().getAsDouble();

            double minAdjacentEnergyRat = maxEnergies.stream().filter(maxEnergy -> maxEnergy.getAdjacentEnergyRat()!= null).mapToDouble(MaxEnergy::getAdjacentEnergyRat).min().getAsDouble();

            if (minDays > 10){
                break;
            }



        }


        return maxEnergies;
    }

    private List<MaxEnergy> merge(List<MaxEnergy> maxEnergies) {
        double minAdjacentEnergyRat = ENERGY_RANG +1;
        maxEnergies = maxEnergies.stream().sorted(Comparator.comparingInt(MaxEnergy::getStartNum)).collect(Collectors.toList());

        /**
         * 计算相邻平均能动变化比
         */
        for (int i = 0; i < maxEnergies.size()-1; i++) {
            MaxEnergy mi = maxEnergies.get(i);
            MaxEnergy mi1 = maxEnergies.get(i + 1);
            mi.setAdjacentEnergyRat(Math.abs(mi.getAvgEnergy()-mi1.getAvgEnergy())/Math.min(mi.getAvgEnergy(),mi1.getAvgEnergy()));
        }

        /**
         * 最后一个需要清空。
         * 根据能量合并
         */
        maxEnergies.get(maxEnergies.size()-1).setAdjacentEnergyRat(null);
        minAdjacentEnergyRat = maxEnergies.stream().filter(maxEnergy -> maxEnergy.getAdjacentEnergyRat()!= null).mapToDouble(MaxEnergy::getAdjacentEnergyRat).min().getAsDouble();
        for (int i = 0; i < maxEnergies.size() - 1; i++) {
            MaxEnergy mi = maxEnergies.get(i);
            MaxEnergy mi1 = maxEnergies.get(i + 1);

            Double adjacentEnergyRat = mi.getAdjacentEnergyRat();
            if (adjacentEnergyRat.equals(minAdjacentEnergyRat)){
                mi.setEndNum(mi1.getEndNum());
                mi.setEndDate(mi1.getEndDate());
                mi.setEnergySum(mi.getEnergySum()+mi1.getEnergySum());
                mi.setAvgEnergy(mi.getEnergySum()/(mi.getEndNum()-mi.getStartNum()+1));
                maxEnergies.remove(i+1);
                break;
            }
        }


        /**
         * 更具时间合并
         */
        double minDays = maxEnergies.stream().filter(maxEnergy -> maxEnergy.getAdjacentEnergyRat()!= null).mapToDouble(m->{return m.getEndNum()-m.getStartNum();}).min().getAsDouble();
        maxEnergies = maxEnergies.stream().sorted(Comparator.comparingInt(MaxEnergy::getStartNum)).collect(Collectors.toList());

        for (int i = 1; i < maxEnergies.size() - 1; i++) {
            MaxEnergy mi = maxEnergies.get(i);


            int days = mi.getEndNum() - mi.getStartNum() ;
            if ( days == minDays && days < 20){
                int cur =0;

                if(maxEnergies.get(i - 1).getAdjacentEnergyRat() > maxEnergies.get(i).getAdjacentEnergyRat())
                {
                    cur = i + 1;
                    MaxEnergy mi1 = maxEnergies.get(cur);
                    mi.setEndNum(mi1.getEndNum());
                    mi.setEndDate(mi1.getEndDate());
                    mi.setEnergySum(mi.getEnergySum()+mi1.getEnergySum());
                    mi.setAvgEnergy(mi.getEnergySum()/(mi.getEndNum()-mi.getStartNum()+1));

                    maxEnergies.remove(cur);
                }else{
                    cur = i - 1;
                    MaxEnergy mi1 = maxEnergies.get(cur);
                    mi.setStartNum(mi1.getStartNum());
                    mi.setStartDate(mi1.getStartDate());
                    mi.setEnergySum(mi.getEnergySum()+mi1.getEnergySum());
                    mi.setAvgEnergy(mi.getEnergySum()/(mi.getEndNum()-mi.getStartNum()+1));

                    maxEnergies.remove(cur);
                }

                continue;
            }
        }

        return maxEnergies;

    }

    private ArrayList<MaxEnergy> findContinuationSection(List<Transaction> transactions) {
        ArrayList<MaxEnergy> maxEnergies = new ArrayList<>();
        transactions= transactions.stream().sorted(Comparator.comparingDouble(o -> o.getReportDate().getTime())).collect(Collectors.toList());

        for (int i = 0; i < transactions.size() ; i++) {
            Transaction t = transactions.get(i);
            double energySum = 0;
            for (int j = i; j < transactions.size() - 1; j++) {
                Transaction tj = transactions.get(j);
                Transaction tj1 = transactions.get(j + 1);
                energySum = energySum + tj.getEnergyDo();
                if (!tj1.getEnergyDo().equals(tj.getEnergyDo())){
                    MaxEnergy maxEnergy = new MaxEnergy();
                    maxEnergy.setStartNum(t.getOderNum());
                    maxEnergy.setStartDate(t.getReportDate());
                    maxEnergy.setEndNum(tj.getOderNum());
                    maxEnergy.setEndDate(tj.getReportDate());
                    maxEnergy.setEnergySum(energySum);
                    maxEnergy.setAvgEnergy(energySum/(maxEnergy.getEndNum() -maxEnergy.getStartNum()+1));
                    maxEnergies.add(maxEnergy);
                    i = j ;
                    break;
                }
            }


        }
        return maxEnergies;
    }

    private void  initFindMaxTranDates(List<Transaction> transactions) {
        double min = transactions.stream().mapToDouble(Transaction::getEnergyDo).min().getAsDouble();
        transactions= transactions.stream().sorted(Comparator.comparingDouble(o -> o.getReportDate().getTime())).collect(Collectors.toList());

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            t.setOderNum(i);
            t.setBei(t.getEnergyDo()/min);
        }

    }


    private List<Transaction> initData(String dataType, List<Transaction> transactions,HashMap<Double, Integer> gradeMap) {
        /**
         * 去掉上市前一个月数据
        */
        if (dataType.contains("1")||dataType.contains("2")){
            transactions= transactions.stream().sorted(Comparator.comparingDouble(o -> o.getReportDate().getTime())).collect(Collectors.toList());
            for (int i = 0; i < 20 && transactions.size()>1; i++) {
                transactions.remove(0);
            }
        }

        /**
         * 平均能动，归一化
         */
        double maxEnergy = transactions.stream().mapToDouble(Transaction::getEnergy).max().getAsDouble();
        double minEnergy = transactions.stream().mapToDouble(Transaction::getEnergy).min().getAsDouble();
        double avgEnergy = transactions.stream().mapToDouble(Transaction::getEnergy).average().getAsDouble();

        /**
         * 收盘价格，归一化
         */
        double maxClose = transactions.stream().mapToDouble(Transaction::getTclose).max().getAsDouble();
        double minClose = transactions.stream().mapToDouble(Transaction::getTclose).min().getAsDouble();
        double avgClose = transactions.stream().mapToDouble(Transaction::getTclose).average().getAsDouble();

        double bei = (maxEnergy - minEnergy) / (maxClose - minClose) ;
        /**
         * 取中位数
         */
        transactions = transactions.stream().sorted(Comparator.comparingDouble(Transaction::getEnergy)).collect(Collectors.toList());
        if (transactions.size()>2){
            transactions.get(0).setEnergy(transactions.get(1).getEnergy());
        }
        Double middleEnergy = transactions.get(transactions.size() / 2).getEnergy();
        HashMap<Integer, Double> defMap = new HashMap<>();
        int num = transactions.size()/ DEF_INIT +1;
        for (int i = 0; i < DEF_INIT; i++) {
            double sum = 0;
            int count = 0;
            for (int j = i*num; j < transactions.size() && j < (i+1)*num; j++) {
                sum = sum + transactions.get(j).getEnergy();
                count++;
            }
            defMap.put(i,sum/count);
            gradeMap.put(sum/count,i);
        }

        for (int i = 0; i < transactions.size(); i++) {
            Transaction tran = transactions.get(i);
            tran.setAvgEnergy(middleEnergy);
            tran.setNorTclose(avgEnergy);
            tran.setEnergyDo(defMap.get(i/num));
        }

        transactions= transactions.stream().sorted(Comparator.comparingLong(o -> o.getReportDate().getTime())).collect(Collectors.toList());
        for (int i = 0; i < slides.size(); i++) {
            Slide slide = slides.get(i);
            int t = 0;
            if (slide.getType().equals("上涨")){
                t = 1;
            }else {
                t = -1;
            }

            for (int j = 0; j < transactions.size(); j++) {
                Transaction tran = transactions.get(j);
                if (tran.getReportDate().getTime() == slide.getStartDate().getTime()){
                    double v = slide.getRange()/100/ slide.getDays();
                    for (int k = 0; k < slide.getDays() && (k+j < transactions.size()); k++) {
                        transactions.get(j+k).setPriceSection(t*v*k);
                    }
                }
            }

        }

        return transactions;
    }

    private boolean union(HashMap<Double, Integer> gradeMap, List<Transaction> transactions) {
        Map<Double, List<Transaction>> listMap = transactions.stream().collect(Collectors.groupingBy(Transaction::getEnergyDo));
        List<Double> list = nearest(listMap.keySet().stream().sorted().collect(Collectors.toList()));

        if (list.size() == 2 &&  (list.get(1) -list.get(0))/list.get(0)< DEF_MIN_AVG_RATE){
            List<Transaction> unionTran = listMap.get(list.get(0));
            unionTran.addAll(listMap.get(list.get(1)));
            double average = unionTran.stream().mapToDouble(Transaction::getEnergyDo).average().getAsDouble();
            gradeMap.put(average,gradeMap.get(list.get(0)));

            transactions.removeAll(unionTran);
            for (int i = 0; i < unionTran.size(); i++) {
                unionTran.get(i).setEnergyDo(average);
            }
            transactions.addAll(unionTran);
            return false;
        }else {
            log.info("{}    最后等级数：{}",securityCode.toString() ,listMap.size());
            return true;
        }
    }

    private List<Double> nearest(List<Double> list) {
        ArrayList<Double> ret = new ArrayList<>();
        if (list.size() < 2 ){
            return ret;
        }
        double minRate = Double.MAX_VALUE;
        int num = 0;
        for (int i = 0; i < list.size()-1; i++) {
            double rate = (list.get(i+1)-list.get(i))/list.get(i);
            if (rate < minRate){
                minRate = rate;
                num = i;
            }
        }
        ret.add(list.get(num));
        ret.add(list.get(num+1));
        return ret;
    }

    /**
     * 修补数据，左右N个数据一样，拉（压）中间数据
     * @param gradeMap
     * @param transactions
     */
    private void repair(HashMap<Double, Integer> gradeMap, List<Transaction> transactions) {
        transactions= transactions.stream().sorted(Comparator.comparingDouble(o -> o.getReportDate().getTime())).collect(Collectors.toList());
        for (int j = 0; j < DEPTH_STEP.length; j++) {
            int depthStep = DEPTH_STEP[j];
            int step = Double.valueOf(depthStep*1.5).intValue();
            for (int i = step; i < transactions.size() - (step + depthStep); i++) {
                ArrayList<Transaction> values = getValues(i,step,depthStep,transactions);
                if(check(gradeMap,step,depthStep,values)){
                    for (int k = 0; k < depthStep; k++) {
                        transactions.get(i+k).setEnergyDo(transactions.get(i+k-1).getEnergyDo());
                    }
                }
            }
        }
    }

    private ArrayList<Transaction> getValues( int i,int step, int depthStep, List<Transaction> transactions) {
        ArrayList<Transaction> values = new ArrayList<>();
        for (int j = i- step; j < transactions.size()- step && j <i+ step +depthStep; j++) {
            values.add(transactions.get(j));
        }
        return values;
    }


    private boolean check(HashMap<Double, Integer> gradeMap, int step, int depthStep,ArrayList<Transaction> values) {
       /* Integer gradeI = gradeMap.get(values.get(MIN_STEP).getMcap());
        Integer gradeI1 = gradeMap.get(values.get(MIN_STEP +1).getMcap());

        Math.abs(gradeI - gradeI1) <= GRADE &&*/

        for (int i = 0; i < depthStep; i++) {
            values.remove(step);
        }

        return  valuesEqual(values);
    }

    private boolean valuesEqual(ArrayList<Transaction> values) {
        for (int i = 0; i < values.size()-1; i++) {
            if(!values.get(i).getEnergyDo().equals(values.get(i + 1).getEnergyDo())){
                return false;
            }
        }
        return true;
    }

    @Data
    public class MaxEnergy {

        /**
         * 开始时间
         */
        private Date startDate;
        private Integer startNum;

        /**
         * 结束时间
         */
        private Date endDate;
        private Integer endNum;


        /**
         * 持续天数
         */
        private Double energySum;

        /**
         * 排名
         */
        private Integer rang;

        /**
         * 区间平均能动
         */
        private Double avgEnergy;

        /**
         * 相邻区间平均能动变化比
         */
        private Double adjacentEnergyRat;


    }

}
