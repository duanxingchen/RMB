package dzr.web.service.impl;


import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.DateUtils;
import dzr.common.utils.MathUtils;
import dzr.holder.entity.Holder;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.info.entity.Event;
import dzr.info.entity.SecurityCode;
import dzr.integration.entity.Departure;
import dzr.integration.entity.StockSelection;
import dzr.transaction.entity.Energy;
import dzr.transaction.entity.Transaction;
import dzr.web.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2022/10/27 16:57
 * @Version 1.0
 * @Description:
 */
@Service
@Primary
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    MonitorServiceImpl monitorService;

    public List<StockSelection> updateStockSelection(StockSelection stockSelection, Holder holder, List<Event> events) {

        List<Event> eventList = events.stream().filter(event -> event.getNoticeDate().getTime() > DateUtils.getNowDate().getTime())
                .sorted(Comparator.comparing(Event::getNoticeDate)).collect(Collectors.toList());
        if (eventList.size() >0){
            Event event = eventList.get(0);
            stockSelection.setEvent(DateUtils.dateToStr(event.getNoticeDate(),"yyyy-MM-dd") + "/"+ event.getEventType()+ "/" + event.getContent());
        }

        stockSelection.setMainForce(holder.getMakerVsSanHuRatio());
        stockSelection.setPrivateFund(holder.getHolderName());
        stockSelection.setFlowMarket(holder.getFlowMarket());
        stockSelection.setIndustryAdvanced(holder.getDongCaiIndustry());
        if (holder.getMakersCost() != null && holder.getPrice() != null)
            stockSelection.setPriceIncrease(MathUtils.doubleRetain2Bit(100*(holder.getPrice()- holder.getMakersCost())/holder.getMakersCost()));
        ArrayList<StockSelection> stockSelections = new ArrayList<>();
        stockSelections.add(stockSelection);
        return stockSelections;
    }

    @Override
    public JSONObject holderChart(String form, List<Holder> holders, List<CompanyInfo> companyInfos, List<ConceptStock> conceptStocks) {
        HashMap<String, ConceptStock> stMap = new HashMap<>();
        conceptStocks.stream().filter(conceptStock -> conceptStock.getConcept().equals("st股")).forEach(conceptStock -> {
            stMap.put(conceptStock.getCode(),conceptStock);
        });
        HashMap<String, CompanyInfo> companyInfoHashMap = new HashMap<>();
        Integer industryCount = JSONObject.parseObject(form).getInteger("industryCount");
        monitorService.updateDongCaiIndustry(industryCount,companyInfos,companyInfoHashMap);
        HashMap<String, Holder> holderHashMap = new HashMap<>();
        holders.stream()
                .filter(holder -> holder.getDongCaiIndustry() != null && holder.getSort12() != 0 && stMap.get(holder.getCode()) == null)
                .forEach(holder -> {
                    holderHashMap.put(holder.getCode(),holder);
                });

        Map<String, List<CompanyInfo>> groupIndustry = companyInfoHashMap.values().stream().collect(Collectors.groupingBy(CompanyInfo::getDongcaiIndustry));
        Map<String, List<ConceptStock>> groupConcept = conceptStocks.stream().collect(Collectors.groupingBy(ConceptStock::getConcept));

        ArrayList<JSONObject> dongCaiIndustry = new ArrayList<>();
        ArrayList<JSONObject> concept = new ArrayList<>();
        String[] xAxis ={"sort12","sort13","sort14","sort15","sort16","sort17","sort18","sort19"};
        ArrayList<String> xAxi = new ArrayList<>(Arrays.asList(xAxis));

        groupIndustry.forEach((industry,companyInfoList) ->{
            ArrayList<Holder> ho = new ArrayList<>();
            companyInfoList.forEach(companyInfo -> {
                Holder holder = holderHashMap.get(companyInfo.getCode());
                if (holder != null){
                    ho.add(holder);
                }
            });
            if(ho.size()>0) {
                JSONObject jsonObject = getJsonObject(industry, ho,companyInfoList.size());
                dongCaiIndustry.add(jsonObject);
            }
        });

        dongCaiIndustry.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("sum")));

        groupConcept.forEach((industry,concepts) ->{
            ArrayList<Holder> ho = new ArrayList<>();
            concepts.forEach(conceptStock -> {
                Holder holder = holderHashMap.get(conceptStock.getCode());
                if (holder != null){
                    ho.add(holder);
                }
            });

            if(ho.size()>0){
                JSONObject jsonObject = getJsonObject(industry, ho,concepts.size());
                concept.add(jsonObject);
            }

        });

        concept.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("sum")));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("xAxi",xAxi);
        jsonObject.put("dongCaiIndustry",dongCaiIndustry);
        jsonObject.put("concept",concept);
        return jsonObject;
    }

    private JSONObject getJsonObject(String industry, List<Holder> ho, int size) {
        JSONObject jsonObject = new JSONObject();
        ArrayList<Double> data = new ArrayList<>();

        data.add(MathUtils.doubleRetain2Bit(ho.stream().mapToDouble(Holder::getSort12).average().getAsDouble()*100-100));
        data.add(MathUtils.doubleRetain2Bit(ho.stream().mapToDouble(Holder::getSort13).average().getAsDouble()*100-100));
        data.add(MathUtils.doubleRetain2Bit(ho.stream().mapToDouble(Holder::getSort14).average().getAsDouble()*100-100));
        data.add(MathUtils.doubleRetain2Bit(ho.stream().mapToDouble(Holder::getSort15).average().getAsDouble()*100-100));
        data.add(MathUtils.doubleRetain2Bit(ho.stream().mapToDouble(Holder::getSort16).average().getAsDouble()*100-100));
        data.add(MathUtils.doubleRetain2Bit(ho.stream().mapToDouble(Holder::getSort17).average().getAsDouble()*100-100));
        data.add(MathUtils.doubleRetain2Bit(ho.stream().mapToDouble(Holder::getSort18).average().getAsDouble()*100-100));
        data.add(MathUtils.doubleRetain2Bit(ho.stream().mapToDouble(Holder::getSort19).average().getAsDouble()*100-100));

        double asDouble = ho.stream().mapToDouble(Holder::getSort19).average().getAsDouble();

        double flowMarket = ho.stream().mapToDouble(Holder::getFlowMarket).sum();
        double tenFlowHolderRatio = ho.stream().mapToDouble(Holder::getTenFlowHolderRatio).average().getAsDouble();
        jsonObject.put("sum",data.stream().mapToDouble(Double::doubleValue).sum());
        jsonObject.put("count",ho.size());
        jsonObject.put("size",size);
        jsonObject.put("name",industry);
        jsonObject.put("tenFlowHolderRatio",tenFlowHolderRatio);
        jsonObject.put("flowMarket",MathUtils.doubleRetain2Bit(flowMarket));
        jsonObject.put("data",data);
        return jsonObject;
    }


    @Override
    public StockSelection getByCodeOrName(List<SecurityCode> securityCodes, String code) {
        StockSelection stockSelection = new StockSelection();
        securityCodes.forEach(securityCode -> {
            if(securityCode.getCode().equals(code) || securityCode.getName().equals(code)){
                stockSelection.setCode(securityCode.getCode());
                stockSelection.setName(securityCode.getName());
                securityCode.setName(securityCode.getName());
            }
        });
        return stockSelection;
    }


    private int calculateUpDownDays( List<Transaction> transactions) {
        int type = transactions.get(0).getChg() > 0.0 ? 1 : -1;
        int upDays = 1;
        for (int i = 1; i < transactions.size(); i++) {
            int typeChange = transactions.get(i).getChg() > 0.0 ? 1 : -1;
            if(type == typeChange){
                upDays++;
            }else {
                break;
            }
        }
        return type*upDays;
    }

    @Override
    public Departure getDeparture(StockSelection stockSelection, List<Transaction> transactions, Holder holder) {
        Departure departure = new Departure();
        departure.setName(stockSelection.getName());
        departure.setCode(stockSelection.getCode());
        Double tenFlowHolderRatio = 0.0;
        if (holder != null){
            tenFlowHolderRatio = holder.getTenFlowHolderRatio() == null? 0.0:holder.getTenFlowHolderRatio();
        }

        double tt = 100/(100 - tenFlowHolderRatio);
        transactions.sort(Comparator.comparing(Transaction::getReportDate).reversed());
        if (transactions.size() > 10){
            departure.setPchg(transactions.get(0).getPchg());
            departure.setChangHands(MathUtils.doubleRetain2Bit(transactions.get(0).getTurnover()*tt));
            double v3 = 100*(transactions.get(0).getTclose() -  transactions.get(2).getTclose())/transactions.get(2).getTclose();
            double d3 = transactions.stream().limit(3).mapToDouble(Transaction::getTurnover).sum();
            departure.setPchg3(MathUtils.doubleRetain2Bit(v3));
            departure.setChangHands3(MathUtils.doubleRetain2Bit(d3*tt));
            double v5 = 100*(transactions.get(0).getTclose() -  transactions.get(4).getTclose())/transactions.get(4).getTclose();
            double d5 = transactions.stream().limit(5).mapToDouble(Transaction::getTurnover).sum();
            departure.setPchg5(MathUtils.doubleRetain2Bit(v5));
            departure.setChangHands5(MathUtils.doubleRetain2Bit(d5*tt));

            double v10 = 100*(transactions.get(0).getTclose() -  transactions.get(9).getTclose())/transactions.get(9).getTclose();
            double d10 = transactions.stream().limit(10).mapToDouble(Transaction::getTurnover).sum();
            departure.setPchg10(MathUtils.doubleRetain2Bit(v10));
            departure.setChangHands10(MathUtils.doubleRetain2Bit(d10*tt));

            double v30 = 100*(transactions.get(0).getTclose() -  transactions.get(29).getTclose())/transactions.get(29).getTclose();
            double d30 = transactions.stream().limit(30).mapToDouble(Transaction::getTurnover).sum();
            departure.setPchg30(MathUtils.doubleRetain2Bit(v30));
            departure.setChangHands30(MathUtils.doubleRetain2Bit(d30*tt));

        }
        departure.setUpDownDays(calculateUpDownDays(transactions));
        return departure;
    }

    @Override
    public List<Holder> filter(JSONObject condition, List<Holder> holders, List<Energy> energyList, List<ConceptStock> conceptStocks) {
        HashMap<String,String> detailCodes = new HashMap<>();
        HashMap<String,String> energyCodes = new HashMap<>();
        HashMap<String,String> conceptCodes = new HashMap<>();
        String concept = condition.getString("concept");
        String detailNum = condition.getString("detailCount");
        String energyNum = condition.getString("energy");
        if (!concept.equals("")){
            conceptStocks.stream().filter(conceptStock -> conceptStock.getConcept().contains(concept))
                    .forEach(conceptStock -> {
                        conceptCodes.put(conceptStock.getCode(),conceptStock.getConcept());
                    });
        }


        if (!energyNum.equals("")) {
            energyList.stream()
                    .sorted(Comparator.comparing(Energy::getRankNum).reversed())
                    .limit(Integer.parseInt(energyNum))
                    .forEach(energy -> {
                        energyCodes.put(energy.getCode(), energy.getCode());
                    });
        }

        String sort = condition.getString("sort");

        List<Holder> collect = holders.stream()
                .filter(holder -> {
            if (concept.equals("") || conceptCodes.get(holder.getCode()) != null){
                return true;
            }
            return false;
        }).filter(holder -> {
            if (detailNum.equals("") || detailCodes.get(holder.getCode()) != null){
                return true;
            }
            return false;
        }).filter(holder -> {
            if (energyNum.equals("") || energyCodes.get(holder.getCode()) != null){
                return true;
            }
            return false;
        }).filter(holder -> {
            String dongCaiIndustry = condition.getString("dongCaiIndustry");
            if (dongCaiIndustry.equals("") ||
                    (holder.getDongCaiIndustry() != null && holder.getDongCaiIndustry().contains(dongCaiIndustry))){
                return true;
            }
            return false;
        }).filter(holder -> {
            String flowMarket = condition.getString("flowMarket");
            if (flowMarket.equals("") ||
                    ( holder.getFlowMarket() != null &&holder.getFlowMarket() < Double.parseDouble(flowMarket))){
                return true;
            }
            return false;
        }).filter(holder -> {
            Boolean delivery = condition.getBoolean("delivery");
            if (delivery){
                if (holder.getHolderName() != null){
                    return true;
                }
                else {
                    return false;
                }
            }

            return true;
        }).filter(holder -> {
                    Boolean st = condition.getBoolean("st");
                    if (st){
                        if (holder.getName() != null && !holder.getName().contains("st")){
                            return true;
                        }
                        else {
                            return false;
                        }
                    }

                    return true;
                }).filter(holder -> {
            String listingDate = condition.getString("listingDate");
            if (listingDate.equals("") ||
                    ( holder.getListingDate() != null && holder.getListingDate().getTime() < DateUtils.getBeforeDate(Integer.parseInt(listingDate)*356).getTime())){
                return true;
            }
            return false;
        }).filter(holder -> {
            String reportDate = condition.getString("reportDate");
            if (reportDate.equals("") || holder.getReportDate().getTime() > DateUtils.getBeforeDate(Integer.parseInt(reportDate)).getTime()){
                return true;
            }
            return false;
        }).filter(holder -> {
            String name = condition.getString("name");
            if (name.equals("") || holder.getName().contains(name)){
                return true;
            }
            return false;
        }).filter(holder -> {
            String priceRatio = condition.getString("priceRatio");
            if (priceRatio.equals("") ||
                    (holder.getPriceRatio() != null && holder.getPriceRatio() < Double.parseDouble(priceRatio))){
                return true;
            }
            return false;
        }).filter(holder -> {
            String sort12 = condition.getString("sort12");
            if (sort12.equals("") ||
                    (holder.getSort12() != null && holder.getSort12() > Double.parseDouble(sort12))){
                return true;
            }
            return false;
        }).filter(holder -> {
                    String sort13 = condition.getString("sort13");
                    if (sort13.equals("") ||
                            (holder.getSort13() != null && holder.getSort13() > Double.parseDouble(sort13))){
                        return true;
                    }
                    return false;
        }).filter(holder -> {
                    String sort14 = condition.getString("sort14");
                    if (sort14.equals("") ||
                            (holder.getSort14() != null && holder.getSort14() > Double.parseDouble(sort14))){
                        return true;
                    }
                    return false;
        }).filter(holder -> {
                    String sort15 = condition.getString("sort15");
                    if (sort15.equals("") ||
                            (holder.getSort15() != null && holder.getSort15() > Double.parseDouble(sort15))){
                        return true;
                    }
                    return false;
        }).filter(holder -> {
                    String sort16 = condition.getString("sort16");
                    if (sort16.equals("") ||
                            (holder.getSort16() != null && holder.getSort16() > Double.parseDouble(sort16))){
                        return true;
                    }
                    return false;
        }).filter(holder -> {
                    String oderNum3 = condition.getString("oderNum3");
                    if (oderNum3.equals("") ||
                            (holder.getOderNum3() != null && holder.getOderNum3() <= Integer.valueOf(oderNum3))){
                        return true;
                    }
                    return false;
        }).filter(holder -> {
                    String oderNum5 = condition.getString("oderNum5");
                    if (oderNum5.equals("") ||
                            (holder.getOderNum5() != null && holder.getOderNum5() <= Integer.valueOf(oderNum5))){
                        return true;
                    }
                    return false;
        }).filter(holder -> {
                    String changePriceWithHolderDown = condition.getString("changePriceWithHolderDown");
                    if (changePriceWithHolderDown.equals("") ||
                            (holder.getChangePriceWithHolderDown() != null && holder.getChangePriceWithHolderDown() >= Double.valueOf(changePriceWithHolderDown)*100.0)){
                        return true;
                    }
                    return false;
        }).filter(holder -> {
                    String sortBigValue = condition.getString("sortBigValue");
                    if (sortBigValue.equals("") || getSortBigValue(holder) >= Double.valueOf(sortBigValue)){
                        return true;
                    }
                    return false;
        }).collect(Collectors.toList());

        return collect;
    }

    private Double getSortBigValue(Holder holder) {
        Double bigValue = 0.0;
        if (holder.getSort12() > bigValue){
            bigValue= holder.getSort12();
        }
        if (holder.getSort13() > bigValue){
            bigValue= holder.getSort13();
        }
        if (holder.getSort14() > bigValue){
            bigValue= holder.getSort14();
        }
        if (holder.getSort15() > bigValue){
            bigValue= holder.getSort15();
        }
        if (holder.getSort16() > bigValue){
            bigValue= holder.getSort16();
        }
        if (holder.getSort17() > bigValue){
            bigValue= holder.getSort17();
        }
        if (holder.getSort18() > bigValue){
            bigValue= holder.getSort18();
        }
        if (holder.getSort19() > bigValue){
            bigValue= holder.getSort19();
        }

        return bigValue;
    }
}
