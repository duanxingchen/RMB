package dzr.web.service.python;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author dzr
 * @Date 2023/4/12 18:10
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
public class LstmMode {
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 预测未来价格
     * @param code
     * @return
     */
    public List<Double> predict(String code){
        String predictPrice = httpClientService.doGetPython("predict/"+code);

        JSONObject jsonObject = JSON.parseObject(predictPrice);
        String retCode = jsonObject.getString("code");
        Double maxValue = jsonObject.getDouble("currentPrice");
        Double future1DaysPrice = jsonObject.getDouble("future1DaysPrice");
        Double future2DaysPrice = jsonObject.getDouble("future2DaysPrice");
        Double future3DaysPrice = jsonObject.getDouble("future3DaysPrice");
        Double future5DaysPrice = jsonObject.getDouble("future5DaysPrice");
        Double future7DaysPrice = jsonObject.getDouble("future7DaysPrice");
        Double future10DaysPrice = jsonObject.getDouble("future10DaysPrice");
        return new ArrayList<>();
    }

}
