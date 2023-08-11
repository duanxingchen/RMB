package dzr.web.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.DateUtils;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.ConceptStockMapper;
import dzr.integration.entity.StockSelection;
import dzr.integration.mapper.StockSelectionMapper;
import dzr.plate.entity.PlateBlock;
import dzr.plate.entity.PlateStock;
import dzr.plate.mapper.PlateBlockMapper;
import dzr.plate.mapper.PlateStockMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import dzr.web.service.MonitorService;
import dzr.web.service.todo.PlateBlockCount;
import dzr.web.service.todo.PlateStockTop;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author dzr
 * @Date 2022/10/9 17:31
 * @Version 1.0
 * @Description:
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor")
public class MonitorController {
    private final CompanyInfoMapper companyInfoMapper;
    private final TransactionMapper transactionMapper;
    private final PlateStockMapper plateStockMapper;
    private final StockSelectionMapper stockSelectionMapper;
    private final PlateBlockMapper plateBlockMapper;
    private final ConceptStockMapper conceptStockMapper;
    private final MonitorService monitorService;



    @RequestMapping(method = RequestMethod.GET,value = "/selectPlateSelfStock")
    public List<PlateStock> selectPlateSelfStock() {
        List<StockSelection> stockSelections = stockSelectionMapper.selectAll();
        ArrayList<String> codes = new ArrayList<>();
        stockSelections.forEach(stockSelection -> codes.add(stockSelection.getCode()));
        List<PlateStock> plateStocks = plateStockMapper.selectByCodes(codes).stream().filter(plateStock -> plateStock.getReportDate().after(DateUtils.getBeforeDate(40))).collect(Collectors.toList());
        return plateStocks;
    }


    @RequestMapping(method = RequestMethod.GET,value = "/selectPlateBlockFromDongCai")
    public List<PlateBlock> selectPlateBlockFromDongCai() {
        List<PlateBlock> stockSelections = plateBlockMapper.selectAll();
        return stockSelections;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/selectPlateBlockCount")
    public JSONArray selectPlateBlockCount() {
        List<CompanyInfo> companyInfos =  companyInfoMapper.selectAll();
        List<PlateStock> plateStocks = plateStockMapper.selectBeforeAllByReportDate(DateUtils.getBeforeDate(40));
        JSONArray json = monitorService.plateBlockCount(companyInfos, plateStocks);
        return json;
    }


    @RequestMapping(method = RequestMethod.POST,value = "/selectPlateBlockHotChart")
    public JSONObject selectPlateBlockHotChart(@RequestBody Date[] dates) {
        List<CompanyInfo> companyInfos =  companyInfoMapper.selectAll();
        List<ConceptStock> conceptStocks = conceptStockMapper.selectAll();
        List<PlateStock> plateStocks = plateStockMapper.selectAllByReportDates(dates[0],dates[1]);
        JSONObject json = monitorService.plateBlockCountHotChart(companyInfos,conceptStocks, plateStocks);
        return json;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/selectPlateBlockDetail/{blackName}")
    public JSONArray selectPlateBlockDetail(@PathVariable("blackName") String blackName) {
        List<CompanyInfo> companyInfos =  companyInfoMapper.selectAll();
        List<String> codes = companyInfos.stream().filter(companyInfo -> companyInfo.getDongcaiIndustry().contains(blackName))
                .map(CompanyInfo::getCode).collect(Collectors.toList());
        List<PlateStock> plateStocks = plateStockMapper.selectByCodes(codes);
        JSONArray json = monitorService.plateBlockDetail(companyInfos, plateStocks);
        return json;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/selectPlateBlock/{blackName}")
    public List<PlateBlockCount> selectPlateBlock(@PathVariable("blackName") String blackName) {
        List<Transaction> transactions = transactionMapper.selectReinstatementByCode("000001");
        List<Date> dates = transactions.stream().map(Transaction::getReportDate).collect(Collectors.toList());
        List<CompanyInfo> companyInfos =  companyInfoMapper.selectAll();
        List<String> codes = companyInfos.stream().filter(companyInfo -> companyInfo.getDongcaiIndustry().equals(blackName))
                .map(CompanyInfo::getCode).collect(Collectors.toList());
        List<PlateStock> plateStocks = plateStockMapper.selectByCodes(codes);
        return monitorService.plateBlock(plateStocks,dates);
    }



    @RequestMapping(method = RequestMethod.GET,value = "/selectPlateStockTop")
    public List<PlateStockTop> selectPlateStockTop() {
        List<PlateStock> plateStocks = plateStockMapper.selectBeforeAllByReportDate(DateUtils.getNowShortDate());

        List<PlateStockTop> plateStockTops = monitorService.getPlateStockTops(plateStocks);

        return plateStockTops;
    }


}
