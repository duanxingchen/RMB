package dzr.plate.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.DateUtils;
import dzr.common.utils.HttpClientService;
import dzr.plate.entity.PlateBlock;
import dzr.plate.mapper.PlateBlockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @Author dzr
 * @Date 2022/10/8 15:24
 * @Version 1.0
 * @Description: 股东人数
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlateBlockInitData {
    private final HttpClientService httpClientService;
    private final PlateBlockMapper plateBlockMapper;

    private final static String url = "http://push2ex.eastmoney.com/getAllBKChanges?ut=7eea3edcaed734bea9cbfc24409ed989&dpt=wzchanges&pageindex=0";

    private String[] include = {
            "融资融券",
            "创业板综",
            "预亏预减",
            "国企改革",
            "富时罗素",
            "机构重仓",
            "预盈预增",
            "广东板块",
            "昨日涨停_含一",
            "标准普尔",
            "次新股",
            "昨日涨停",
            "板块",
            "深股通",
            "昨日连板_含一","概念","qfii重仓","华为"};


    public void remountPullDataFromWeb() {
        ArrayList<PlateBlock> plateBlocks = new ArrayList<>();
        String ret = httpClientService.doGet(url).toLowerCase();

        JSONObject data = JSON.parseObject(ret).getJSONObject("data");
        JSONArray allBk = data.getJSONArray("allbk");

        for (int i = 0; i < allBk.size(); i++) {
            JSONObject info = allBk.getJSONObject(i);
            PlateBlock plateBlock = new PlateBlock();
            if (isInclude(info.getString("n"))){
                continue;
            }
            plateBlock.setPlateName(info.getString("n"));
            plateBlock.setPlateCode(info.getString("c"));
            plateBlock.setReportDate(DateUtils.getNowShortDate());
            plateBlock.setNetCap(info.getDouble("zjl")/10000);
            plateBlock.setPchg(info.getDouble("u"));
            plateBlock.setPlateNum(info.getInteger("ct"));
            JSONObject ms = info.getJSONObject("ms");
            plateBlock.setCode(ms.getString("c"));
            plateBlock.setName(ms.getString("n"));
            plateBlock.setSocketType(getChinese(ms.getInteger("t")));

            JSONArray ydl = info.getJSONArray("ydl");

            for (int j = 0; j < ydl.size(); j++) {
                JSONObject ydlJSONObject = ydl.getJSONObject(j);
                Integer ct = ydlJSONObject.getInteger("ct");
                int t = ydlJSONObject.getInteger("t");

                switch (t){
                    case 8201:
                        //火箭发射
                        plateBlock.setRocketLaunch(ct);
                        break;
                    case 8202:
                        //快速反弹
                        plateBlock.setRapidRebound(ct);
                        break;
                    case 8203:
                        //高台跳水
                        plateBlock.setHighDiving(ct);
                        break;
                    case 8204:
                        //加速下跌
                        plateBlock.setAccelerateDecline(ct);
                        break;
                    case 8207:
                        //竞价上涨
                        plateBlock.setPallingUp(ct);
                        break;
                    case 8208:
                        //竞价下跌
                        plateBlock.setPallingDown(ct);
                        break;
                    case 8209:
                        //高开5日线
                        plateBlock.setHighOpened5(ct);
                        break;
                    case 8210:
                        plateBlock.setLowOpened5(ct);
                        //低开5日线
                        break;
                    case 8211:
                        //向上缺口
                        plateBlock.setUpwardNotch(ct);
                        break;
                    case 8212:
                        //向下缺口
                        plateBlock.setDownwardNotch(ct);
                        break;
                    case 8213:
                        //60日新高
                        plateBlock.setDayHigh60(ct);
                        break;
                    case 8214:
                        //60日新低
                        plateBlock.setDayLow60(ct);
                        break;
                    case 8215:
                        //60日大幅上涨
                        plateBlock.setSharpRise60(ct);
                        break;
                    case 8216:
                        //60日大幅下跌
                        plateBlock.setSharpDrop60(ct);
                        break;
                    case 8217:
                        break;
                    case 8218:

                        break;
                    case 8193:
                        //大笔买入
                        plateBlock.setBigBuy(ct);
                        break;
                    case 8194:
                        //大笔卖出
                        plateBlock.setBigSales(ct);
                        break;


                    case 4:
                        //封涨停板
                        plateBlock.setHighLimit(ct);
                        break;
                    case 8:
                        //封跌停板
                        plateBlock.setDownLimit(ct);
                        break;
                    case 16:
                        //打开涨停板
                        plateBlock.setOpenHighLimit(ct);
                        break;
                    case 32:
                        //打开跌停板
                        plateBlock.setOpenDownLimit(ct);
                        break;
                    case 64:
                        //有大买盘
                        plateBlock.setBuyBid(ct);
                        break;
                    case 128:
                        //有大卖盘
                        plateBlock.setSalesBid(ct);
                        break;
                    default:
                        log.warn("ERROR CODE {}",t);
                }
            }
            plateBlockMapper.delete(plateBlock);
            plateBlocks.add(plateBlock);
        }

        plateBlockMapper.batchInsert(plateBlocks);
    }

    private boolean isInclude(String blockName) {
        for (int i = 0; i < include.length; i++) {
            String s = include[i];
            if (blockName.contains(s)){
                return true;
            }
        }
        return false;
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
                break;
            case 8218:

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
