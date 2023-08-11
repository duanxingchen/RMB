package dzr.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author dzr
 * @Date 2022/10/8 14:20
 * @Version 1.0
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "spring.http-pool")
@Data
public class HttpClientProperties {
    //默认配置
    private int defaultMaxPerRoute = 2;
    private int maxTotal = 20;
    private int validateAfterInactivity = 2000;
    private int connectTimeout = 20000;
    private int connectionRequestTimeout = 20000;
    private int socketTimeout = 20000;

}
