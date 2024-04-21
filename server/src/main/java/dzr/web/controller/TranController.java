package dzr.web.controller;


import com.alibaba.fastjson.JSONObject;
import dzr.holder.entity.Holder;
import dzr.holder.mapper.HolderMapper;
import dzr.holder.mapper.HolderNumMapper;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.ConceptStockMapper;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.integration.mapper.StockSelectionMapper;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import dzr.plate.mapper.PlateStockMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import dzr.web.service.TranService;
import dzr.web.service.todo.Form;
import dzr.web.service.todo.TranFrom;
import groovy.util.logging.Log4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/tran")
@Log4j
public class TranController {

    private final CompanyInfoMapper companyInfoMapper;
    private final HolderMapper holderMapper;
    private final HolderNumMapper holderNumMapper;
    private final OrganizationDetailsHolderMapper organizationDetailsHolderMapper;
    private final TransactionMapper transactionMapper;
    private final SecurityCodeMapper securityCodeMapper;
    private final PlateStockMapper plateStockMapper;
    private final StockSelectionMapper stockSelectionMapper;
    private final ConceptStockMapper conceptStockMapper;
    private final TranService tranService;


    @RequestMapping(method = RequestMethod.POST,value = "/selectTranHotChartByDate")
    public JSONObject selectTranHotChartByDate(@RequestBody Form form) {
        List<CompanyInfo> companyInfos =  companyInfoMapper.selectAll();
        List<ConceptStock> conceptStocks = conceptStockMapper.selectAll();
        List<Transaction> transactions = transactionMapper.selectReinstatementDataTypeByReportDates(form.getDates()[0],form.getDates()[1]);
        List<Holder> holders = holderMapper.selectAll();
        return tranService.holderChart(form.getIndustryCount(),companyInfos,conceptStocks,transactions,holders);
    }


    @RequestMapping(method = RequestMethod.POST,value = "/selectTranHotChartByCodes")
    public JSONObject selectTranHotChartByCodes(@RequestBody TranFrom tranFrom) {
        List<Holder> holders = holderMapper.selectAll();
        ArrayList<String> codes = new ArrayList<>();
        for (int i = 0; i < tranFrom.getCodes().length; i++) {
            codes.add(tranFrom.getCodes()[i]);
        }
        List<Transaction> transactions = transactionMapper.selectReinstatementDataTypeByCodesAndReportDates(codes,tranFrom.getDates()[0],tranFrom.getDates()[1]);
        return tranService.tranChart(transactions,holders);
    }



}
