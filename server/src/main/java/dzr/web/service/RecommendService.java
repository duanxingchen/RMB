package dzr.web.service;

import com.alibaba.fastjson.JSONObject;
import dzr.holder.entity.Holder;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.info.entity.Event;
import dzr.info.entity.SecurityCode;
import dzr.integration.entity.Departure;
import dzr.integration.entity.StockSelection;
import dzr.transaction.entity.Energy;
import dzr.transaction.entity.Transaction;

import java.util.List;

/**
 * @Author dzr
 * @Date 2022/10/27 16:56
 * @Version 1.0
 * @Description:
 */
public interface RecommendService {
    StockSelection getByCodeOrName(List<SecurityCode> securityCodes, String code);

    Departure getDeparture(StockSelection stockSelection, List<Transaction> transactions, Holder holder);

    List<Holder> filter(JSONObject condition, List<Holder> holders, List<Energy> energyList, List<ConceptStock> conceptStocks);

    public List<StockSelection> updateStockSelection(StockSelection stockSelection, Holder holder, List<Event> events);




    JSONObject holderChart(List<Holder> holders, List<CompanyInfo> companyInfos, List<ConceptStock> conceptStocks);
}
