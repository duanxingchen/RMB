package dzr.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author dzr
 * @Date 2021/11/25 9:46
 * @Version 1.0
 * @Description:
 */
public class ThreadPools {

    private volatile static ThreadPoolExecutor executor = null;
    private final static int corePoolSize = 2;
    private final static int maximumPoolSize = 2;
    private final static int keepAliveTime = 60;
    private final static int capacity =  5;

    public static ThreadPoolExecutor getExecutor(){
        if (executor == null){
            synchronized (ThreadPools.class){
                if (executor == null){
                    executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                            new ArrayBlockingQueue<Runnable>(capacity),new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
        return executor;
    }

}
