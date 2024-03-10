package dzr.quantization.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.DateUtils;
import dzr.common.utils.MathUtils;
import dzr.common.utils.RobotsService;
import dzr.holder.entity.HolderOrg;
import dzr.holder.mapper.HolderOrgMapper;
import dzr.quantization.entity.StockPool;
import dzr.quantization.init.RealtimeAcquisitionStockPrices;
import dzr.quantization.mapper.StockPoolMapper;
import dzr.web.controller.MonitorController;
import dzr.web.controller.TranController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 1.股东人数
 * 2.板块热点
 * 3.跌买
 * 4.钉钉
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TradingStockService {
    private final StockPoolMapper stockPoolMapper;
    private final HolderOrgMapper holderOrgMapper;
    private final TranController tranController;
    private final TransDays transDays;
    private final MonitorController monitorController;

    private final RealtimeAcquisitionStockPrices realtimeAcquisitionStockPrices;
    /**
     *股价跌幅
     */
    private final static double PRICE_DOWN_RATE = 0.1;

    public void noticePrice(){
        /**
         * 读取标的股票
         */
        List<StockPool> stockPools = stockPoolMapper.selectAll();
        stockPools = realtimeAcquisitionStockPrices.remountPullDataFromWeb(stockPools);
        /**
         * 计算板块热点，推送股票
         */

        String textPrice = getBlockPrice(5) + getBlockPrice(3);

        RobotsService.send(textPrice);

        String textPlate = getBlockPlat(5) + getBlockPlat(3);
        RobotsService.send(textPlate);

        String textPlateVariance = getBlockPlatVariance(5) + getBlockPlatVariance(3);
        RobotsService.send(textPlateVariance);


        /**
         * 个股价格异动波动，推送股票
         */
        stockPools.forEach(stockPool -> {
            HolderOrg holderOrg = holderOrgMapper.selectOneByCode(stockPool.getCode());

            /**
             * 钉钉推送
             */if (holderOrg != null && (holderOrg.getTenDayMaxPrice() - stockPool.getPrice())/stockPool.getPrice() > PRICE_DOWN_RATE){
                RobotsService.send(stockPool.toString());
            }
        });

    }

    private String getBlockPrice(int num ) {
        Date[] dates = {DateUtils.dateToDate(transDays.getBeforeDate(num),"yyyy-MM-dd"),DateUtils.dateToDate(DateUtils.getNowDate(),"yyyy-MM-dd")};
        JSONObject selectTranHotChartByDate = tranController.selectTranHotChartByDate(dates);
        JSONArray dongCaiIndustry = selectTranHotChartByDate.getJSONArray("dongCaiIndustry");
        String text = "\n\n" + num + "天板块涨幅 \n";
        for (int i = dongCaiIndustry.size() - 1 ; i > dongCaiIndustry.size() -10; i--) {
            JSONObject jsonObject = dongCaiIndustry.getJSONObject(i);
            text = text + jsonObject.getString("name") + "  " + jsonObject.getString("sum") + "  " + jsonObject.getString("count") + "  \n";
        }
        return text;
    }


    private String getBlockPlat(int num ) {
        Date[] dates = {DateUtils.dateToDate(transDays.getBeforeDate(num),"yyyy-MM-dd"),DateUtils.dateToDate(DateUtils.getNowDate(),"yyyy-MM-dd")};
        JSONObject selectTranHotChartByDate = monitorController.selectPlateBlockHotChart(dates);
        JSONArray dongCaiIndustry = selectTranHotChartByDate.getJSONArray("dongCaiIndustry");
        String text = "\n\n" + num + "天板块异动 \n";
        for (int i = dongCaiIndustry.size() - 1 ; i > dongCaiIndustry.size() -10; i--) {
            JSONObject jsonObject = dongCaiIndustry.getJSONObject(i);
            text = text + jsonObject.getString("name") + "  " + jsonObject.getString("sum") + "  " + jsonObject.getString("allCount") + "  \n";
        }
        return text;
    }


    /**
     * 异动方差
     * @param num
     * @return
     */
    private String getBlockPlatVariance(int num ) {
        Date[] dates = {DateUtils.dateToDate(transDays.getBeforeDate(num),"yyyy-MM-dd"),DateUtils.dateToDate(DateUtils.getNowDate(),"yyyy-MM-dd")};
        JSONObject selectTranHotChartByDate = monitorController.selectPlateBlockHotChart(dates);
        JSONArray dongCaiIndustry = selectTranHotChartByDate.getJSONArray("dongCaiIndustry");
        String text = "\n\n" + num + "天板块异动方差 \n";
        ArrayList<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < dongCaiIndustry.size() ; i++) {
            JSONObject jsonObject = dongCaiIndustry.getJSONObject(i);
            JSONArray allInt = jsonObject.getJSONArray("allInt");
            //JSONArray normalizer = normalizer(allInt);
            jsonObject.put("Variance",variance(allInt));
            list.add(jsonObject);
        }
        list.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("Variance")));

        for (int i = list.size() - 1 ; i > list.size() -10; i--) {
            JSONObject jsonObject = list.get(i);
            text = text + jsonObject.getString("name") + "  " + jsonObject.getString("Variance") + "  " + jsonObject.getString("allCount") + "  \n";
        }
        return text;
    }

    public JSONArray normalizer(JSONArray allInt){
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < allInt.size(); i++) {
            Double aDouble = allInt.getDouble(i);
            if (aDouble > max){
                max = aDouble;
            }
            if (aDouble < min){
                min = aDouble;
            }
        }
        JSONArray array = new JSONArray();
        for (int i = 0; i < allInt.size(); i++) {
            array.add((allInt.getDouble(i) - min) /(max - min));
        }
        return array;
    }

    public static double variance(JSONArray x) {
        int m=x.size();
        if(m == 0){
            return 0.0;
        }
        double sum=0;

        for(int i=0;i<m;i++){//求和
                sum += x.getDouble(i);
         }

        double dAve=sum/m;//求平均值
        double dVar=0;

        for(int i=0;i<m;i++){//求方差
            dVar+=(x.getDouble(i)-dAve)*(x.getDouble(i)-dAve);
        }

        return MathUtils.doubleRetain2Bit(Math.sqrt(dVar/m));
    }


}
