package org.openea.eap.framework.mq.redis.config;

import org.openea.eap.framework.mq.redis.core.RedisMQTemplate;
import org.openea.eap.framework.mq.redis.core.interceptor.RedisMessageInterceptor;
import org.openea.eap.framework.redis.config.EapRedisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * Redis 消息队列 Producer 配置类
 *
 */
@Slf4j
@AutoConfiguration(after = EapRedisAutoConfiguration.class)
public class EapRedisMQProducerAutoConfiguration {

    @Bean
    public RedisMQTemplate redisMQTemplate(StringRedisTemplate redisTemplate,
                                           List<RedisMessageInterceptor> interceptors) {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redisTemplate);
        // 添加拦截器
        interceptors.forEach(redisMQTemplate::addInterceptor);
        return redisMQTemplate;
    }

}
