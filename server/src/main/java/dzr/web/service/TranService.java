package dzr.web.service;

import com.alibaba.fastjson.JSONObject;
import dzr.holder.entity.Holder;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.transaction.entity.Transaction;

import java.util.List;

/**
 * @Author dzr
 * @Date 2023/4/1 11:08
 * @Version 1.0
 * @Description:
 */
public interface TranService {
    JSONObject holderChart(Integer industryCount, List<CompanyInfo> companyInfos, List<ConceptStock> conceptStocks, List<Transaction> transactions, List<Holder> holders);

    JSONObject tranChart(List<Transaction> transactions, List<Holder> holders);
}
