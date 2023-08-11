package dzr.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dzr.holder.entity.Holder;
import dzr.holder.mapper.HolderMapper;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.info.entity.Event;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.ConceptStockMapper;
import dzr.info.mapper.EventMapper;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.integration.entity.Departure;
import dzr.integration.entity.StockSelection;
import dzr.integration.mapper.DepartureMapper;
import dzr.integration.mapper.StockSelectionMapper;
import dzr.transaction.entity.Energy;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.EnergyMapper;
import dzr.transaction.mapper.TransactionMapper;
import dzr.web.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    private final StockSelectionMapper stockSelectionMapper;
    private final DepartureMapper departureMapper;
    private final HolderMapper holderMapper;
    private final SecurityCodeMapper securityCodeMapper;
    private final RecommendService recommendService;
    private final TransactionMapper transactionMapper;
    private final EnergyMapper energyMapper;
    private final EventMapper eventMapper;
    private final CompanyInfoMapper companyInfoMapper;
    private final ConceptStockMapper conceptStockMapper;


    @RequestMapping(method = RequestMethod.GET,value = "/stockSelection/selectAll")
    public List<StockSelection> selectAllStockSelection() {
        List<StockSelection> stockSelections = stockSelectionMapper.selectAll();
        stockSelections.forEach(stockSelection -> {
            Holder holder = holderMapper.selectOneByCode(stockSelection.getCode());
            List<Event> events = eventMapper.selectByCode(stockSelection.getCode());
            List<StockSelection> list = recommendService.updateStockSelection(stockSelection, holder, events);
            stockSelectionMapper.batchDelete(list);
            stockSelectionMapper.batchInsert(list);
        });
        return stockSelectionMapper.selectAll();
    }


    @RequestMapping(method = RequestMethod.PUT,value = "/addStock")
    public String addStock(@RequestBody() String request) {
        List<SecurityCode> securityCodes = securityCodeMapper.selectAll();
        StockSelection stockSelection = recommendService.getByCodeOrName(securityCodes, JSON.parseObject(request).getString("code"));
        Holder holder = holderMapper.selectOneByCode(stockSelection.getCode());
        List<Event> events = eventMapper.selectByCode(stockSelection.getCode());
        List<StockSelection> stockSelections = recommendService.updateStockSelection(stockSelection, holder, events);
        stockSelectionMapper.batchDelete(stockSelections);
        stockSelectionMapper.batchInsert(stockSelections);
        return "success";
    }



    @RequestMapping(method = RequestMethod.GET,value = "/departure/selectAll")
    public List<Departure> selectAllDeparture() {
        ArrayList<Departure> departures = new ArrayList<>();
        List<StockSelection> stockSelections = stockSelectionMapper.selectAll();
        stockSelections.forEach(stockSelection -> {
            List<Transaction> transactions = transactionMapper.selectReinstatementByCode(stockSelection.getCode());
            Holder holder = holderMapper.selectOneByCode(stockSelection.getCode());

            Departure departure = recommendService.getDeparture(stockSelection,transactions,holder);
            departures.add(departure);
        });
        return departures;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/selectHolderChart")
    public JSONObject selectHolderChart(@RequestBody String form) {
        List<CompanyInfo> companyInfos =  companyInfoMapper.selectAll();
        List<ConceptStock> conceptStocks = conceptStockMapper.selectAll();
        List<Holder> holders = selectHolderAll(form);
       return recommendService.holderChart(holders,companyInfos,conceptStocks);
    }



    @RequestMapping(method = RequestMethod.POST,value = "/selectHolderAll")
    public List<Holder> selectHolderAll(@RequestBody String form) {
        JSONObject condition = JSON.parseObject(form);
        List<Energy>  energyList =  energyMapper.selectAll();

        List<ConceptStock> conceptStocks = conceptStockMapper.selectAll();
        List<Holder> holders = holderMapper.selectAll();
        return recommendService.filter(condition,holders,energyList,conceptStocks);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/deleteStock/{code}")
    public String deleteStock(@PathVariable("code") String code) {
        ArrayList<StockSelection> stockSelections = new ArrayList<>();
        StockSelection stockSelection = new StockSelection();
        stockSelection.setCode(code);
        stockSelections.add(stockSelection);
        stockSelectionMapper.batchDelete(stockSelections);
        return "SUCCESS";
    }
}
