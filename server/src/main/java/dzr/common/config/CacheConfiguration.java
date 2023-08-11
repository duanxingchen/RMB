package dzr.common.config;

import com.google.common.cache.CacheBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author dzr
 * @Date 2022/10/10 16:23
 * @Version 1.0
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "guava.cache.config")
@Data
public class CacheConfiguration {

    private int concurrencyLevel;

    private long maximumSize;

    private long expireAfterWrite;

    private long refreshAfterWrite;

    private int initialCapacity;


    @Bean
    public CacheManager cacheManager() {
        System.out.println(concurrencyLevel);
        System.out.println(maximumSize);
        System.out.println(expireAfterWrite);
        System.out.println(refreshAfterWrite);
        System.out.println(initialCapacity);

        CacheBuilder<Object, Object> ObjectCacheBuilder = CacheBuilder.newBuilder()
                // 存活时间（30秒内没有被访问则被移除）
                .expireAfterAccess(refreshAfterWrite, TimeUnit.SECONDS)
                // 存活时间（写入10分钟后会自动移除）
                .expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES)
                // 最大size
                .maximumSize(maximumSize)
                // 最大并发量同时修改
                .concurrencyLevel(concurrencyLevel)
                // 初始化大小为100个键值对
                .initialCapacity(initialCapacity);
                // 变成软引用模式（在jvm内存不足时会被回收）
                //.softValues();

        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager.setCacheBuilder(ObjectCacheBuilder);
        return cacheManager;
    }
}
