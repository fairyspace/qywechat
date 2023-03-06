package io.github.fairyspace.qywechat.config;

import io.github.fairyspace.qywechat.exceptions.AesException;
import io.github.fairyspace.qywechat.utils.WXBizJsonMsgCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

@Configuration
public class Common {

    @Value("${wechat.AppToken}")
    String appToken;
    @Value("${wechat.CorpID}")
    String corpID;
    @Value("${wechat.EncodingAESKey}")
    String encodingAESKey;

    @Bean
    public WXBizJsonMsgCrypt getWxBizJsonMsgCrypt() throws AesException {
        return new WXBizJsonMsgCrypt(appToken, encodingAESKey, corpID);
    }

    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor() {
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "pool-" + r.hashCode());
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<>(16), threadFactory, rejectedExecutionHandler);
        return threadPoolExecutor;
    }


    @Bean
    public RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(30 * 1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }


}
