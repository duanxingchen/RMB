package dzr.info.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @Author dzr
 * @Date 2022/10/8 15:24
 * @Version 1.0
 * @Description: 股票信息
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityCodeInitData {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final static String url = "http://33.push2.eastmoney.com/api/qt/clist/get?po=1&np=1&fltt=2&invt=2&pn=1&pz=8000&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f2,f12,f14";


    public void remountPullDataFromWeb() {
        String ret = httpClientService.doGet(url).toLowerCase();
        ArrayList<SecurityCode> securityCodes = new ArrayList<>();

        JSONArray data = JSON.parseObject(ret).getJSONObject("data").getJSONArray("diff");

        for (int i = 0; i < data.size(); i++) {
            JSONObject info = data.getJSONObject(i);
            if (!info.getString("f2").equals("-")){
                SecurityCode securityCode = new SecurityCode();
                securityCode.setCode(info.getString("f12"));
                securityCode.setName(info.getString("f14"));
                if(!securityCode.getCode().startsWith("8")){
                    securityCodes.add(securityCode);
                }
            }
        }
        securityCodeMapper.batchInsert(securityCodes);
    }
}
