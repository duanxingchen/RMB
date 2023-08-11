package dzr.info.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import dzr.info.entity.Event;
import dzr.info.mapper.EventMapper;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventInitData  {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final EventMapper eventMapper;
    private final static String url = "https://emweb.securities.eastmoney.com/PC_HSF10/CompanyBigNews/GetDSTX?code=$code&type=0&pageIndex=1";


    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            String urlPre = url.replace("$code", securityCode.getCodeWithExchange());
            String ret = httpClientService.doGet(urlPre).toLowerCase();
            ArrayList<Event> events = new ArrayList<>();
            if (ret.contains("data")){
                JSONArray data = JSON.parseObject(ret).getJSONObject("dstx").getJSONArray("data");

                for (int i = 0; i < data.size(); i++) {
                    JSONArray array = data.getJSONArray(i);
                    for (int j = 0; j <array.size() ; j++) {
                        JSONObject info = array.getJSONObject(j);
                        Event event = new Event();
                        event.setCode(securityCode.getCode());
                        event.setName(securityCode.getName());
                        event.setEventType(info.getString("event_type"));
                        event.setContent(info.getString("level1_content"));
                        event.setNoticeDate(info.getDate("notice_date"));
                        events.add(event);
                    }
                }
            }
            eventMapper.batchInsert(events);
        });
    }
}
