package dzr.integration.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2023/4/12 18:10
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j

public class PythonNormalization {

    @Autowired
    private HttpClientService httpClientService;

    public List<Double> normalization(String code ,List<Double> orgData){
        List<NameValuePair> list = new LinkedList<>();
        list.add( new BasicNameValuePair("price",orgData.toString()));
        list.add( new BasicNameValuePair("code",code));

        /**
         * 调用Python方法
         */
        String priceNormal = httpClientService.doPostPython("normalization",list);

        ArrayList<Double> priceWithStep = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(priceNormal);
        String retCode = jsonObject.getString("code");
        Double maxValue = jsonObject.getDouble("maxValue");
        Double minValue = jsonObject.getDouble("minValue");
        JSONArray normal = jsonObject.getJSONArray("normal");
        return normal.stream().map(one -> Double.valueOf(one.toString())).collect(Collectors.toList());
    }

}
