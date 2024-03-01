package dzr.common.utils;

import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


/**
 * @program: springboot
 * @description: httpClient通信工具类
 * @author: dzr
 * @create: 2022-10-10 17:18
 * <p>
 * 注：设置httpClient的工具类。提供了get和post访问的静态方法。
 * get请求 Content-Type==text/html;charset=UTF-8
 * post请求 Content-Type=application/json;charset=UTF-8
 * 可以灵活的设置socket-timeout（socket连接时间，即超时时间,单位毫秒！）
 */
@Service
public class HttpClientService {
    private final static Logger log = LoggerFactory.getLogger(HttpClientService.class);

    private final static String PYTHON_WEB_HOST = "http://192.168.1.10:18188";

    private  CloseableHttpClient httpClient = new DefaultHttpClient();

    //private RequestConfig config = new RequestConfig();



    /**
     * get 请求  Content-Type==text/html;charset=UTF-8
     *
     * @param url    url地址
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public  String doGet(String url) {
        try {
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setCharset(Consts.UTF_8).build();
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            //设置请求头
            //httpGet.setConfig(config);
            httpGet.addHeader("Cookie","focus-certification-pop=-1; Hm_lvt_c3f6328a1a952e922e996c667234cdae=1676600093,1678764202; rz_utm_source=10003; ss_utm_campaign=S-品牌计划-B-PC; ss_utm_sign=baidu; ss_utm_platform=pc; ss_utm_term=私募排排网; ss_utm_content=品专词; ss_bd_vid=11332906562447847715; ss_bd_login_url=https://www.simuwang.com/?utm_source=10003&utm_medium=cpc&utm_campaign=S-%E5%93%81%E7%89%8C%E8%AE%A1%E5%88%92-B-PC&utm_sign=baidu&utm_platform=pc&utm_term=%E7%A7%81%E5%8B%9F%E6%8E%92%E6%8E%92%E7%BD%91&utm_content=%E5%93%81%E4%B8%93%E8%AF%8D&e_crowdid=3507912&bd_vid=11332906562447847715; smppw_tz_auth=1; http_tK_cache=0f864d504045ae470c8ae759009ec5ffc5ce461f; cur_ck_time=1678774435; ck_request_key=TqcA/0yspKYiKyJhZ3rIVqFH58xWD5wsYUFBXid06YI=; passport=2289561\tu7567169905521\tBQUFBQwAAQldUwQBWlBWDFJcAQEGCgkJAFZeXgYHAVY=9710d4c4e7; rz_rem_u_p=ioAGMixUlfo0HIAzhIy1vXM2ydGpoQstH1U+Doh6Vx0=$4r0M6nUP/ZHNMxH4VR6pfhCcc9Q5vNGMivgE0k7Q6Yo=; certification=1; qualified_investor=1; evaluation_result=5; Hm_lpvt_c3f6328a1a952e922e996c667234cdae=1678782076");
            httpGet.addHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            httpGet.addHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
            log.debug(url);
            return doHttp(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public  String doGetPython(String path) {
        return doGet(PYTHON_WEB_HOST+"/"+path);
    }

    public  String doPostPython(String path,List<NameValuePair> list) {
        return doPost(PYTHON_WEB_HOST+"/"+path,list);
    }

    public  String doPost(String url,List<NameValuePair> list) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setCharset(Consts.UTF_8).build();
            HttpPost httpPost = new HttpPost(uriBuilder.build());
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(entityParam);
            //httpPost.setConfig(config);
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            log.info("url:{}  data:{} ",url,entityParam.toString());
            String ret = doHttp(httpPost);
            log.info("python return :{} ",ret.length());
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 实际上调用远程的方法
     *
     * @param request       httpGet/httpPost的共同父类
     * @return
     * @throws IOException
     */
    private String doHttp(HttpRequestBase request) throws IOException {
        ResponseHandler<String> handler = new BasicResponseHandler();
        long startPoint = System.currentTimeMillis();
        String response = httpClient.execute(request, handler);
        log.debug("请求耗时【{}】", System.currentTimeMillis() - startPoint);
        log.debug("接口返回信息【{}】", response);
        return response;
    }
}
