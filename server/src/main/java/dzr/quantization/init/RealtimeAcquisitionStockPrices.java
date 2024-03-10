package dzr.quantization.init;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import dzr.holder.entity.HolderNum;
import dzr.info.entity.SecurityCode;
import dzr.quantization.entity.StockPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时获取股票价格，1分钟拉取一次
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RealtimeAcquisitionStockPrices {
    private final HttpClientService httpClientService;
    private final static String url = "https://push2.eastmoney.com/api/qt/ulist/get?fltt=1&invt=2&fields=f14%2Cf12%2Cf13%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf124&secids=$code&pn=1&np=1&pz=20";


    public ArrayList<StockPool> remountPullDataFromWeb(List<StockPool> securityCodeList) {
        String codes = "";
        for (int i = 0; i < securityCodeList.size(); i++) {
            if (i != securityCodeList.size() -1){
                codes = codes + securityCodeList.get(i).getCodeWithNumber() + "%2C";
            }else {
                codes = codes + securityCodeList.get(i).getCodeWithNumber();
            }
        }

        String urlPre = url.replace("$code", codes);
        String ret = httpClientService.doGet(urlPre).toLowerCase();
        ArrayList<StockPool> stockPools = new ArrayList<>();
        if (ret.contains("data")) {
            JSONArray data = JSON.parseObject(ret).getJSONObject("data").getJSONArray("diff");
            for (int i = 0; i < data.size(); i++) {
                JSONObject info = data.getJSONObject(i);
                StockPool stockPool = new StockPool();
                stockPool.setCode(info.getString("f12"));
                stockPool.setName(info.getString("f14"));
                stockPool.setPrice(info.getDouble("f2")/100.00);
                stockPool.setPriceRange(info.getDouble("f3")/100.00);
                stockPools.add(stockPool);
            }
        }
        return stockPools;
    }
}
