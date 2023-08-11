package dzr.common.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author dzr
 * @Date 2022/9/19 10:23
 * @Version 1.0
 * @Description:
 */
public class RobotsService {
    private final static Logger log = LoggerFactory.getLogger(RobotsService.class);

    final static String sessionWebHook = "https://oapi.dingtalk.com/robot/send?access_token=7354018fdd6995b46bf5e4a5a8f826dab655c8e60f172aa1f9098f9a8768c13a";
    final static DingTalkClient client = new DefaultDingTalkClient(sessionWebHook);

    public static void send(String content) {
        try {
            content = "内容: " +"\n" + content;
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(content);
            request.setText(text);
            OapiRobotSendResponse response = client.execute(request);
            log.info(response.getBody());
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
