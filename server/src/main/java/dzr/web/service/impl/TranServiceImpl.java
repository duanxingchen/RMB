package dzr.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.MathUtils;
import dzr.holder.entity.Holder;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.transaction.entity.Transaction;
import dzr.web.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2023/4/1 11:17
 * @Version 1.0
 * @Description:
 */
@Service
@Primary
public class TranServiceImpl implements TranService {

    @Autowired
    MonitorServiceImpl monitorService;


    @Override
    public JSONObject holderChart(Integer industryCount, List<CompanyInfo> companyInfos, List<ConceptStock> conceptStocks, List<Transaction> transactions, List<Holder> holders) {

        HashMap<String, CompanyInfo> companyInfoHashMap = new HashMap<>();
        monitorService.updateDongCaiIndustry(industryCount, companyInfos,companyInfoHashMap);
        HashMap<String, List<Transaction>> transactionHashMap = new HashMap<>();
        HashSet<Date> reportDates = new HashSet<>();
        HashMap<String, Double> tenFlowHolderRatioHashMap = tenFlowHolderRatioHashMap(holders);

        transactions.stream().map(transaction -> {
            reportDates.add(transaction.getReportDate());
            Double aDouble = tenFlowHolderRatioHashMap.get(transaction.getCode());
            if (aDouble != null){
                transaction.setTurnover(MathUtils.doubleRetain2Bit(transaction.getTurnover()*aDouble));
            }
            return transaction;
        }).collect(Collectors.groupingBy(Transaction::getCode)).forEach((code, trans) ->
            transactionHashMap.put(code,trans));

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
            ArrayList<Transaction> trans = new ArrayList<>();
            infos.forEach(info->{
                List<Transaction> list = transactionHashMap.get(info.getCode());
                if (list != null){
                    trans.addAll(list);
                }
            });

            JSONObject jsonObject = getIndustryData(sortDates, industry, infos.size(), trans);
            dongCaiIndustry.add(jsonObject);
        });

        dongCaiIndustry.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("sum")));

        groupConcept.forEach((industry,concepts) ->{
            ArrayList<Transaction> trans = new ArrayList<>();
            concepts.forEach(conceptStock->{
                List<Transaction> list = transactionHashMap.get(conceptStock.getCode());
                if (list != null){
                    trans.addAll(list);
                }
            });

            JSONObject jsonObject = getIndustryData(sortDates, industry, concepts.size(), trans);
            concept.add(jsonObject);
        });

        concept.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("sum")));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("xAxi",sortDates);
        jsonObject.put("dongCaiIndustry",dongCaiIndustry);
        jsonObject.put("concept",concept);
        return jsonObject;
    }

    private HashMap<String, Double> tenFlowHolderRatioHashMap(List<Holder> holders) {
        HashMap<String,Double> tenFlowHolderRatioHashMap = new HashMap<>();

        holders.forEach(holder -> {
            Double tenFlowHolderRatio = 0.0;
            if (holder != null){
                tenFlowHolderRatio = holder.getTenFlowHolderRatio() == null? 0.0:holder.getTenFlowHolderRatio();
            }

            double tt = 100/(100 - tenFlowHolderRatio);
            tenFlowHolderRatioHashMap.put(holder.getCode(),tt);

        });
        return tenFlowHolderRatioHashMap;
    }


    private JSONObject getIndustryData(List<Date> sortDates, String industry, int count, ArrayList<Transaction> trans) {
        JSONObject jsonObject = new JSONObject();
        ArrayList<Double> turnOverData = new ArrayList<>();
        ArrayList<Double> pchgData = new ArrayList<>();
        Map<Date, List<Transaction>> listMap = trans.stream().collect(Collectors.groupingBy(Transaction::getReportDate));
        sortDates.forEach(date -> {
            List<Transaction> transactions = listMap.get(date);
            if (transactions != null){
                turnOverData.add(MathUtils.doubleRetain2Bit(transactions.stream().mapToDouble(Transaction::getTurnover).average().getAsDouble()));
                pchgData.add(MathUtils.doubleRetain2Bit(transactions.stream().mapToDouble(Transaction::getPchg).average().getAsDouble()));

            }else {
                turnOverData.add(0.0);
                pchgData.add(0.0);
            }
        });

        jsonObject.put("sum", MathUtils.doubleRetain2Bit(pchgData.stream().mapToDouble(Double::doubleValue).sum()));
        jsonObject.put("count",count);
        jsonObject.put("name",industry);
        jsonObject.put("pchgData",pchgData);
        jsonObject.put("turnOverData",turnOverData);
        return jsonObject;
    }

    @Override
    public JSONObject tranChart(List<Transaction> transactions, List<Holder> holders) {
        HashMap<String, Double> tenFlowHolderRatioHashMap = tenFlowHolderRatioHashMap(holders);

        HashSet<Date> reportDates = new HashSet<>();
        ArrayList<JSONObject> data = new ArrayList<>();

        Map<String, List<Transaction>> codeTranMap = transactions.stream().map(transaction -> {
            reportDates.add(transaction.getReportDate());
            Double aDouble = tenFlowHolderRatioHashMap.get(transaction.getCode());
            if (aDouble != null){
                transaction.setTurnover(MathUtils.doubleRetain2Bit(transaction.getTurnover()*aDouble));
            }
            return transaction;
        }).collect(Collectors.groupingBy(Transaction::getCode));
        List<Date> sortDates = reportDates.stream().sorted(Comparator.comparing(Date::getTime)).collect(Collectors.toList());

        codeTranMap.forEach((code,trans)->{
            ArrayList<Double> turnOverData = new ArrayList<>();
            ArrayList<Double> pchgData = new ArrayList<>();
            trans.sort(Comparator.comparing(Transaction::getReportDate));
            HashMap<Date, Transaction> dateTransactionHashMap = new HashMap<>();
            trans.forEach(transaction -> dateTransactionHashMap.put(transaction.getReportDate(),transaction));
            sortDates.forEach(date -> {
                Transaction transaction = dateTransactionHashMap.get(date);
                if (transaction != null){
                    turnOverData.add(transaction.getTurnover());
                    pchgData.add(transaction.getPchg());
                }else {
                    turnOverData.add(0.0);
                    pchgData.add(0.0);
                }
            });


            JSONObject oneDataJson = new JSONObject();
            oneDataJson.put("pchgSum", MathUtils.doubleRetain2Bit(pchgData.stream().mapToDouble(Double::doubleValue).sum()));
            oneDataJson.put("turnOverSum", MathUtils.doubleRetain2Bit(turnOverData.stream().mapToDouble(Double::doubleValue).sum()));

            oneDataJson.put("name",trans.get(0).getName());
            oneDataJson.put("pchgData",pchgData);
            oneDataJson.put("turnOverData",turnOverData);
            data.add(oneDataJson);
        });
        data.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("pchgSum")));
        JSONObject outJson = new JSONObject();
        outJson.put("xAxi",sortDates);
        outJson.put("data",data);
        return outJson;
    }
}
