package dzr.info.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import dzr.info.entity.ConceptStock;
import dzr.info.mapper.ConceptStockMapper;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConceptStockInitData  {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final ConceptStockMapper conceptStockMapper;
    private final static String url = "http://push2.eastmoney.com/api/qt/slist/get?spt=3&pi=0&fields=f14&secid=$code";

    private final static String out = "融资融券" +
            "预盈预增" +
            "富时罗素" +
            "创业板综" +
            "预亏预减" +
            "标准普尔" +
            "机构重仓" +
            "深股通" +
            "广东板块" +
            "浙江板块" +
            "沪股通" +
            "江苏板块" +
            "msci中国" +
            "华为概念" +
            "中证500" +
            "转债标的" +
            "深成500" +
            "长江三角" +
            "上证380" +
            "节能环保" +
            "国企改革" +
            "专精特新" +
            "深圳特区" +
            "电商概念" +
            "创投" +
            "hs300_" +
            "qfii重仓" +
            "证金持股" +
            "股权激励" +
            "参股银行" +
            "上证180_" +
            "低价股" +
            "注册制次新股" +
            "昨日涨停_含一字" +
            "基金重仓" +
            "深证100r" +
            "央视50_" +
            "上证50_" +
            "参股券商" +
            "参股新三板" +
            "5g概念" +
            "ah股" +
            "北交所概念" +
            "昨日连板" +
            "昨日连板_含一字" +
            "破净股" +
            "板块" +
            "高送转" +
            "hs300_" +
            "深证100r" +
            "青海板块" +
            "宁夏板块" +
            "西藏板块" +
            "海南板块" +
            "甘肃板块" +
            "贵州板块" +
            "山西板块" +
            "广西板块" +
            "云南板块" +
            "吉林板块" +
            "新疆板块" +
            "重庆板块" +
            "天津板块" +
            "河北板块" +
            "陕西板块" +
            "江西板块" +
            "辽宁板块" +
            "河南板块" +
            "湖北板块" +
            "湖南板块" +
            "安徽板块" +
            "四川板块" +
            "福建板块" +
            "山东板块" +
            "上海板块" +
            "北京板块" +
            "沪证" +
            "ah股"+
            "北京冬奥"+
            "债转股"+
            "沪企改革"+
            "昨日触板"+
            "参股保险"+
            "ab股"+
            "ppp模式"+
            "参股期货"+
            "口罩"+
            "msci中国"+
            "qfii重仓"+
            "黑龙江"+
            "创业成分"+
            "内蒙古"+
            "股权转让"+
            "茅指数"+
            "世界杯"+
            "举牌";

    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            String urlPre = url.replace("$code", securityCode.getCodeWithNumber());
            String ret = httpClientService.doGet(urlPre).toLowerCase();
            ArrayList<ConceptStock> conceptStocks = new ArrayList<>();
            if (ret.contains("diff")){
                JSONObject data = JSON.parseObject(ret).getJSONObject("data");
                JSONObject diff = data.getJSONObject("diff");

                for (int i = 0; i < diff.size(); i++) {
                    JSONObject info = diff.getJSONObject(String.valueOf(i));

                    ConceptStock conceptStock = new ConceptStock();
                    conceptStock.setCode(securityCode.getCode());
                    conceptStock.setName(securityCode.getName());
                    conceptStock.setConcept(info.getString("f14"));
                    if (!out.contains(conceptStock.getConcept())){
                        conceptStocks.add(conceptStock);
                    }
                }
            }
            try {
                if (conceptStocks.size() > 0) {
                    conceptStockMapper.batchInsert(conceptStocks);
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }

        });
    }
}
