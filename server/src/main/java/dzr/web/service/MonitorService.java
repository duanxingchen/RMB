package dzr.web.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.ConceptStock;
import dzr.plate.entity.PlateStock;
import dzr.web.service.todo.PlateBlockCount;
import dzr.web.service.todo.PlateStockTop;

import java.util.Date;
import java.util.List;

/**
 * @Author dzr
 * @Date 2022/12/8 17:38
 * @Version 1.0
 * @Description:
 */
public interface MonitorService {
    JSONArray plateBlockCount(List<CompanyInfo> companyInfos, List<PlateStock> plateStocks);

    List<PlateBlockCount> plateBlock(List<PlateStock> plateStocks, List<Date> dates);

    JSONArray plateBlockDetail(List<CompanyInfo> companyInfos, List<PlateStock> plateStocks);
    List<PlateStockTop> getPlateStockTops(List<PlateStock> plateStocks);

    JSONObject plateBlockCountHotChart(List<CompanyInfo> companyInfos, List<ConceptStock> conceptStocks, List<PlateStock> plateStocks);

}
