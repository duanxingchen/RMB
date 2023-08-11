package dzr.plate.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.DateUtils;
import dzr.common.utils.HttpClientService;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.plate.entity.PlateStock;
import dzr.plate.mapper.PlateStockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author dzr
 * @Date 2022/10/8 15:24
 * @Version 1.0
 * @Description: 股东人数
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlateStockInitData  {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final PlateStockMapper plateStockMapper;

    private final static String dateUrl = "http://push2ex.eastmoney.com/getStockStatisticsChanges?ut=7eea3edcaed734bea9cbfc24409ed989&startdate=$startDate&enddate=20500101&dpt=wzchanges&code=$code&market=$market";
    private final static String url = "http://push2ex.eastmoney.com/getStockChanges?ut=7eea3edcaed734bea9cbfc24409ed989&date=$reportDate&dpt=wzchanges&code=$code&market=$market";

    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            ArrayList<PlateStock> plateStocks = new ArrayList<>();
            Date newestReportDate =  getNewestReportDatesFromTable(securityCode);
            List<String> reportDates = getReportDates(securityCode,newestReportDate);

            for (int i = 0; i < reportDates.size(); i++) {
                String urlPre = url.replace("$code", securityCode.getCode())
                        .replace("$reportDate",reportDates.get(i))
                        .replace("$market", securityCode.getCode().startsWith("6")? "1":"0");
                String ret = httpClientService.doGet(urlPre).toLowerCase();

                JSONObject out = JSON.parseObject(ret).getJSONObject("data");
                if (out != null){
                    JSONArray in = out.getJSONArray("data");

                    for (int j = 0; j < in.size(); j++) {
                        JSONObject info = in.getJSONObject(j);

                        PlateStock plateStock = new PlateStock();
                        plateStock.setName(out.getString("n"));
                        plateStock.setCode(out.getString("c"));
                        plateStock.setReportDate(DateUtils.strNotToDate(out.getString("d") + " " + info.getString("tm")));

                        plateStock.setVo(info.getDouble("v"));
                        plateStock.setPrice(info.getDouble("p")/1000);
                        plateStock.setPchg(info.getDouble("u"));
                        plateStock.setPlate(getChinese(info.getInteger("t")));
                        plateStock.setDetail(info.getString("t"));
                        plateStocks.add(plateStock);
                    }
                }
            }
            plateStockMapper.batchInsert(plateStocks);
        });

    }

    private Date getNewestReportDatesFromTable(SecurityCode securityCode){
        PlateStock plateStock = plateStockMapper.selectNewestByCode(securityCode.getCode());
        if (plateStock == null || plateStock.getReportDate() == null){
            return securityCode.getListingDate();
        }
        return plateStock.getReportDate();
    }
    private List<String> getReportDates(SecurityCode securityCode, Date newestReportDate) {
        ArrayList<String> dates = new ArrayList<>();
        String urlPre = dateUrl.replace("$code", securityCode.getCode())
                .replace("$startDate",DateUtils.dateToStr(newestReportDate,"yyyyMMdd"))
                .replace("$market", securityCode.getCode().startsWith("6")? "1":"0");
        String ret = httpClientService.doGet(urlPre).toLowerCase();

        JSONObject out = JSON.parseObject(ret).getJSONObject("data");
        if (out != null){
            JSONArray data = out.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject info = data.getJSONObject(i);
                dates.add(info.getString("d"));
            }
        }

        return dates;
    }

    private String getChinese(Integer t) {
        String str ="";
        switch (t){
            case 8201:
                str = "火箭发射";
                break;
            case 8202:
                str = "快速反弹";
                break;
            case 8203:
                str = "高台跳水";
                break;
            case 8204:
                str = "加速下跌";
                break;
            case 8207:
                str = "竞价上涨";
                break;
            case 8208:
                str = "竞价下跌";
                break;
            case 8209:
                str = "高开5日线";
                break;
            case 8210:
                str = "低开5日线";
                break;
            case 8211:
                str = "向上缺口";
                break;
            case 8212:
                str = "向下缺口";
                break;
            case 8213:
                str = "60日新高";
                break;
            case 8214:
                str = "60日新低";
                break;
            case 8215:
                str = "60日大幅上涨";
                break;
            case 8216:
                str = "60日大幅下跌";
                break;
            case 8217:
                str = "竞价异常";
                break;
            case 8218:
                str = "竞价异常";
                break;
            case 8193:
                str = "大笔买入";
                break;
            case 8194:
                str = "大笔卖出";
                break;


            case 4:
                str = "封涨停板";
                break;
            case 8:
                str = "封跌停板";
                break;
            case 16:
                str = "打开涨停板";
                break;
            case 32:
                str = "打开跌停板";
                break;
            case 64:
                str = "有大买盘";
                break;
            case 128:
                str = "有大卖盘";
                break;
            default:
                log.warn("ERROR CODE {}",t);
        }
        return str;
    }
}
