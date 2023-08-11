package dzr.common.config;

import lombok.RequiredArgsConstructor;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author dzr
 * @Date 2022/10/8 14:18
 * @Version 1.0
 * @Description:
 */
@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

    private final HttpClientProperties httpClientProperties;


    /**
     * 显示修改httpClient连接池参数，注：若未显示设置，应该有默认配置！
     *
     * @return
     */
    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setDefaultMaxPerRoute(httpClientProperties.getDefaultMaxPerRoute());
        httpClientConnectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
        httpClientConnectionManager.setValidateAfterInactivity(httpClientProperties.getValidateAfterInactivity());
        return httpClientConnectionManager;
    }


    //设置网络配置器
    @Bean
    public RequestConfig requestConfig(){
        return RequestConfig.custom().setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())  //从链接池获取连接的超时时间
                .setConnectTimeout(httpClientProperties.getConnectTimeout())    //与服务器连接超时时间，创建socket连接的超时时间
                .setSocketTimeout(httpClientProperties.getSocketTimeout())   //socket读取数据的超时时间，从服务器获取数据的超时时间
                .setExpectContinueEnabled(true)    //设置是否开启 客户端在发送Request Message之前，先判断服务器是否愿意接受客户端发送的消息主体
                .build();
    }

    /**
     * 实例化连接池，设置连接池管理器
     *
     * @param poolingHttpClientConnectionManager
     * @return
     */
    @Bean
    public HttpClientBuilder httpClientBuilder(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //设置连接池
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        //设置超时时间
        httpClientBuilder.setDefaultRequestConfig(requestConfig());
        //定义连接管理器将由多个客户端实例共享。如果连接管理器是共享的，则其生命周期应由调用者管理，如果客户端关闭则不会关闭。
        httpClientBuilder.setConnectionManagerShared(true);
        //设置Keep-Alive
        ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {

            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch(NumberFormatException ignore) {
                        }
                    }
                }
                HttpHost target = (HttpHost) context.getAttribute(
                        HttpClientContext.HTTP_TARGET_HOST);
                if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
                    // Keep alive for 5 seconds only
                    return 5 * 1000;
                } else {
                    // otherwise keep alive for 30 seconds
                    return 30 * 1000;
                }
            }

        };
        httpClientBuilder.setKeepAliveStrategy(myStrategy);
        return httpClientBuilder;
    }


    @Bean
    public CloseableHttpClient getCloseableHttpClient(HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

}
