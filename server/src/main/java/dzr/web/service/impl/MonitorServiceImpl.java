package dzr.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.DateUtils;
import dzr.common.utils.MathUtils;
import dzr.holder.entity.Holder;
import dzr.holder.mapper.HolderMapper;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.plate.entity.PlateStock;
import dzr.plate.mapper.PlateStockMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import dzr.web.service.MonitorService;
import dzr.web.service.todo.PlateBlockCount;
import dzr.web.service.todo.PlateCount;
import dzr.web.service.todo.PlateStockTop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2022/12/8 17:46
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class MonitorServiceImpl implements MonitorService {
    private final TransactionMapper transactionMapper;
    private final HolderMapper holderMapper;
    private final PlateStockMapper plateStockMapper;

    List<CompanyInfo> companyInfos;

    private static Integer[] periods = {0,1,2,3,5,7,10,15,20,25,30};
    private static String up = "火箭发射";
    private static String down = "高台跳水";


    public List<PlateCount> groupByPeriod(List<PlateStock> plateStocks, int period) {
        Date beforeDate = DateUtils.dateToDate(DateUtils.getBeforeDate(period),"yyyy-MM-dd");
        Map<String, List<PlateStock>> industryMap = plateStocks.stream()
                .filter(plateStock -> plateStock.getReportDate().after(beforeDate))
                .collect(Collectors.groupingBy(PlateStock::getDongcaiIndustry));
        ArrayList<PlateCount> plateCounts = new ArrayList<>();

        industryMap.forEach((industry,ps)->{
            PlateCount plateCount = new PlateCount();
            plateCount.setConcept(industry);
            plateCount.setPlateNum(ps.size());
            int upPlate = 0;
            for (int i = 0; i < ps.size(); i++) {
                if (up.contains(ps.get(i).getPlate())) {
                    upPlate++;
                }
            }
           plateCount.setUpPlate(upPlate);
            long stockNum = companyInfos.stream().filter(companyInfo -> companyInfo.getDongcaiIndustry().equals(industry))
                    .count();
            plateCount.setStockNum(stockNum);
            plateCount.setRate(MathUtils.doubleRetain2Bit(100*upPlate/stockNum));
            plateCounts.add(plateCount);
        });
        plateCounts.sort(Comparator.comparing(PlateCount::getRate).reversed());
        return plateCounts;
    }

    @Override
    public JSONArray plateBlockCount(List<CompanyInfo> companyInfos, List<PlateStock> plateStocks) {
        this.companyInfos = companyInfos;
        HashMap<String, CompanyInfo> map = new HashMap<>();
        updateDongCaiIndustry(companyInfos,map);

        plateStocks.forEach(plateStock -> plateStock.setDongcaiIndustry(map.get(plateStock.getCode()).getDongcaiIndustry()));
        JSONArray array = new JSONArray();
        for (int i = 0; i < periods.length; i++) {
            JSONObject jsonObject = new JSONObject();
            List<PlateCount> plateCounts = groupByPeriod(plateStocks, periods[i]);
            jsonObject.put("name",String.valueOf(periods[i]));
            jsonObject.put("data",plateCounts);
            array.add(jsonObject);
        }
        return array;
    }

    /**
     * 根据东财行业，拆分东财分组
     * @param companyInfos
     * @param map
     */
    public void updateDongCaiIndustry(List<CompanyInfo> companyInfos, HashMap<String, CompanyInfo> map) {
        companyInfos.forEach(companyInfo -> {
            if (companyInfo.getDongcaiIndustry() != null){
                map.put(companyInfo.getCode(),companyInfo);
            }
        });
        Map<String, List<CompanyInfo>> companyMap = map.values().stream().collect(Collectors.groupingBy(CompanyInfo::getDongcaiIndustry));
        /**
         * 当行业板块少于50支股票时，向上合并行业板块
         */
        companyMap.forEach((industry, company) -> {
            if (company.size() < 50){
                company.forEach(companyInfo -> {
                    String[] split = companyInfo.getDongcaiIndustry().split("-");
                    companyInfo.setDongcaiIndustry(split[0] + "-" + split[1]);
                    map.put(companyInfo.getCode(),companyInfo);
                });
            }
        });

        companyMap = map.values().stream().collect(Collectors.groupingBy(CompanyInfo::getDongcaiIndustry));
        companyMap.forEach((industry, company) -> {
            if (company.size() < 50){
                company.forEach(companyInfo -> {
                    String[] split = companyInfo.getDongcaiIndustry().split("-");
                    companyInfo.setDongcaiIndustry(split[0]);
                    map.put(companyInfo.getCode(),companyInfo);
                });
            }
        });
    }

    @Override
    public List<PlateBlockCount> plateBlock(List<PlateStock> plateStocks, List<Date> dates) {
        HashMap<Long, PlateBlockCount> countHashMap = new HashMap<>();
        long minDate = plateStocks.stream().mapToLong(p -> p.getReportDate().getTime()).min().getAsLong();

        dates.stream().filter(date -> date.getTime() > minDate).forEach(date -> {
            PlateBlockCount count = new PlateBlockCount();
            count.setReportDate(date);
            countHashMap.put(date.getTime(),count);
        });

        plateStocks.stream().map(plateStock -> {
            PlateStock plate = new PlateStock();
            plate.setPlate(plateStock.getPlate());
            plate.setReportDate(DateUtils.dateToDate(plateStock.getReportDate(),"yyyy-MM-dd"));
            return plate;
        }).collect(Collectors.groupingBy(PlateStock::getReportDate))
                .forEach((reportDate,ps)->{
                    PlateBlockCount plateBlockCount = new PlateBlockCount();
                    plateBlockCount.setReportDate(reportDate);
                    plateBlockCount.setPlateNum(ps.size());
                    long upPlate = ps.stream().filter(p -> up.contains(p.getPlate())).count();
                    plateBlockCount.setUpPlate(upPlate);
                    countHashMap.put(reportDate.getTime(),plateBlockCount);
                });
        List<PlateBlockCount> plateBlockCounts = countHashMap.values().stream().sorted(Comparator.comparing(PlateBlockCount::getReportDate)).collect(Collectors.toList());
        return plateBlockCounts;
    }

    @Override
    public JSONArray plateBlockDetail(List<CompanyInfo> companyInfos, List<PlateStock> plateStocks) {
        this.companyInfos = companyInfos;
        JSONArray array = new JSONArray();
        for (int i = 0; i < periods.length; i++) {
            JSONObject jsonObject = new JSONObject();
            List<PlateCount> plateCounts = groupByPeriodDetail(plateStocks, periods[i]);
            jsonObject.put("name",String.valueOf(periods[i]));
            jsonObject.put("data",plateCounts);
            array.add(jsonObject);
        }
        return array;
    }

    public List<PlateCount> groupByPeriodDetail(List<PlateStock> plateStocks, int period) {
        Date beforeDate = DateUtils.dateToDate(DateUtils.getBeforeDate(period),"yyyy-MM-dd");
        Map<String, List<PlateStock>> nameMap = plateStocks.stream()
                .filter(plateStock -> plateStock.getReportDate().after(beforeDate))
                .collect(Collectors.groupingBy(PlateStock::getName));
        ArrayList<PlateCount> plateCounts = new ArrayList<>();


        long count = plateStocks.stream()
                .filter(plateStock -> plateStock.getReportDate().after(beforeDate) && up.contains(plateStock.getPlate()))
                .count();
        if (count ==0){
            return plateCounts;
        }

        nameMap.forEach((name,ps)->{
            PlateCount plateCount = new PlateCount();
            plateCount.setConcept(name);
            plateCount.setPlateNum(ps.size());
            int upPlate = 0;
            for (int i = 0; i < ps.size(); i++) {
                if (up.contains(ps.get(i).getPlate())) {
                    upPlate++;
                }
            }
            plateCount.setUpPlate(upPlate);
            plateCount.setStockNum(count);
            plateCount.setRate(MathUtils.doubleRetain2Bit(100*upPlate/count));
            plateCounts.add(plateCount);
        });
        plateCounts.sort(Comparator.comparing(PlateCount::getRate).reversed());
        return plateCounts;
    }

    public List<PlateStockTop> getPlateStockTops(List<PlateStock> plateStocks) {
        ArrayList<PlateStockTop> plateStockTops = new ArrayList<>();
        Map<String, List<PlateStock>> map = plateStocks.stream().filter(plateStock -> plateStock.getPlate().equals("火箭发射")).collect(Collectors.groupingBy(PlateStock::getCode));
        map.forEach((code, plateStocks1) ->{
            PlateStockTop plateStockTop = new PlateStockTop();
            PlateStock plateStock = plateStocks1.get(0);
            plateStockTop.setCode(code);
            plateStockTop.setName(plateStock.getName());
            plateStockTop.setReportDate(plateStock.getReportDate());
            plateStockTop.setFireTimes(plateStocks1.size());
            List<Transaction> transactions = transactionMapper.selectReinstatementByCode(code);
            List<PlateStock> plateStocks2 = plateStockMapper.selectByCode(code);
            Holder holder = holderMapper.selectOneByCode(code);
            Double tenFlowHolderRatio = 0.0;
            if (holder != null){
                plateStockTop.setFlowMarket(holder.getFlowMarket());
                plateStockTop.setDongcaiIndustry(holder.getDongCaiIndustry());
                tenFlowHolderRatio = holder.getTenFlowHolderRatio() == null? 0.0:holder.getTenFlowHolderRatio();
            }

            double tt = 100/(100 - tenFlowHolderRatio);

            transactions.sort(Comparator.comparing(Transaction::getReportDate).reversed());
            if (transactions.size() >5){
                plateStockTop.setPchg(transactions.get(0).getPchg());
                plateStockTop.setChangHands(MathUtils.doubleRetain2Bit(transactions.get(0).getTurnover()*tt));
                double v3 = 100*(transactions.get(0).getTclose() -  transactions.get(2).getTclose())/transactions.get(2).getTclose();
                double d3 = (transactions.get(0).getTurnover() + transactions.get(1).getTurnover() + transactions.get(2).getTurnover());
                plateStockTop.setPchg3(MathUtils.doubleRetain2Bit(v3));
                plateStockTop.setChangHands3(MathUtils.doubleRetain2Bit(d3*tt));
                double v5 = 100*(transactions.get(0).getTclose() -  transactions.get(4).getTclose())/transactions.get(4).getTclose();
                double d5 = (transactions.get(0).getTurnover() + transactions.get(1).getTurnover() + transactions.get(2).getTurnover()
                        + transactions.get(3).getTurnover() + transactions.get(4).getTurnover());
                plateStockTop.setPchg5(MathUtils.doubleRetain2Bit(v5));
                plateStockTop.setChangHands5(MathUtils.doubleRetain2Bit(d5*tt));

            }

            Map<Date, List<PlateStock>> dateListMap = plateStocks2.stream().filter(ps -> ps.getPlate().equals("火箭发射"))
                    .map(ps -> {
                        ps.setReportDate(DateUtils.dateToDate(ps.getReportDate(), "yyyy-MM-dd"));
                        return ps;
                    }).collect(Collectors.groupingBy(PlateStock::getReportDate));

            List<Date> dates = dateListMap.keySet().stream().sorted(Comparator.comparing(Date::getTime).reversed()).collect(Collectors.toList());
            if (dates.size() >3){
                plateStockTop.setFireDays(DateUtils.differentDays( dates.get(1),plateStock.getReportDate()));
                plateStockTop.setFireDays1(DateUtils.differentDays(dates.get(2),plateStock.getReportDate()));
            }
            plateStockTops.add(plateStockTop);

        } );
        return plateStockTops;
    }

    @Override
    public JSONObject plateBlockCountHotChart(List<CompanyInfo> companyInfos, List<ConceptStock> conceptStocks, List<PlateStock> plateStocks) {
        HashMap<String, CompanyInfo> companyInfoHashMap = new HashMap<>();
        updateDongCaiIndustry(companyInfos,companyInfoHashMap);
        HashMap<String, List<PlateStock>> plateStockHashMap = new HashMap<>();
        HashSet<Date> reportDates = new HashSet<>();

        plateStocks.stream().map(plateStock -> {
            Date date = DateUtils.dateToDate(plateStock.getReportDate(), "yyyy-MM-dd");
            plateStock.setReportDate(date);
            reportDates.add(date);
            return plateStock;
        }).collect(Collectors.groupingBy(PlateStock::getCode)).forEach((code, stocks) ->
                plateStockHashMap.put(code,stocks));

        List<Date> sortDates = reportDates.stream().sorted(Comparator.comparing(Date::getTime)).collect(Collectors.toList());
        /**
         * 东财分组
         */
        Map<String, List<CompanyInfo>> groupIndustry = companyInfoHashMap.values().stream().collect(Collectors.groupingBy(CompanyInfo::getDongcaiIndustry));
        /**
         * 概念分组
         */
        Map<String, List<ConceptStock>> groupConcept = conceptStocks.stream().collect(Collectors.groupingBy(ConceptStock::getConcept));

        ArrayList<JSONObject> dongCaiIndustry = new ArrayList<>();
        ArrayList<JSONObject> concept = new ArrayList<>();

        groupIndustry.forEach((industry,infos) ->{
            ArrayList<PlateStock> stocks = new ArrayList<>();
            infos.forEach(info->{
                List<PlateStock> list = plateStockHashMap.get(info.getCode());
                if (list != null){
                    stocks.addAll(list);
                }
            });

            JSONObject jsonObject = getOneIndustryData(sortDates, industry, infos.size(), stocks);
            dongCaiIndustry.add(jsonObject);
        });

        dongCaiIndustry.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("sum")));

        groupConcept.forEach((industry,concepts) ->{
            ArrayList<PlateStock> stocks = new ArrayList<>();
            concepts.forEach(conceptStock->{
                List<PlateStock> list = plateStockHashMap.get(conceptStock.getCode());
                if (list != null){
                    stocks.addAll(list);
                }
            });

            JSONObject jsonObject = getOneIndustryData(sortDates, industry, concepts.size(), stocks);
            concept.add(jsonObject);
        });

        concept.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("sum")));


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("xAxi",sortDates);
        jsonObject.put("dongCaiIndustry",dongCaiIndustry);
        jsonObject.put("concept",concept);
        return jsonObject;
    }

    private JSONObject getOneIndustryData(List<Date> sortDates, String industry, int stockNum, ArrayList<PlateStock> plateStocks) {
        JSONObject jsonObject = new JSONObject();
        ArrayList<Integer> upInt = new ArrayList<>();
        ArrayList<Integer> downInt = new ArrayList<>();
        ArrayList<Integer> allInt = new ArrayList<>();

        HashSet<String> upSet = new HashSet<>();
        HashSet<String> downSet = new HashSet<>();
        HashSet<String> allSet = new HashSet<>();
        Map<Date, List<PlateStock>> dateListMap = plateStocks.stream().collect(Collectors.groupingBy(PlateStock::getReportDate));

        sortDates.forEach(date -> {
            List<PlateStock> stocks = dateListMap.get(date);
            if (stocks != null){
                int upPlate = 0;
                int downPlate = 0;
                int allPlate = 0;

                for (int j = 0; j < stocks.size(); j++) {
                    if (up.contains(stocks.get(j).getPlate())) {
                        upSet.add(stocks.get(j).getCode());
                        upPlate++;
                    }
                    if (down.contains(stocks.get(j).getPlate())) {
                        downSet.add(stocks.get(j).getCode());
                        downPlate++;
                    }
                    allSet.add(stocks.get(j).getCode());
                }
                upPlate = upPlate *100/stockNum;
                downPlate = downPlate *100 /stockNum;
                allPlate = upPlate + downPlate;
                upInt.add(upPlate);
                downInt.add(downPlate);
                allInt.add(allPlate);
            }
        });

        jsonObject.put("sum",upInt.stream().mapToInt(Integer::intValue).sum());
        jsonObject.put("name",industry);
        jsonObject.put("upCount",upSet.size());
        jsonObject.put("downCount",downSet.size());
        jsonObject.put("allCount",allSet.size());
        jsonObject.put("size",stockNum);
        jsonObject.put("upInt",upInt);
        jsonObject.put("downInt",downInt);
        jsonObject.put("allInt",allInt);

        return jsonObject;
    }
}
