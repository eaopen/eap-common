package org.openea.eap.framework.redis.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "jsonRedisTemplate")
    private RedisTemplate<String, Object> jsonRedisTemplate;

    @Resource(name = "jdkRedisTemplate")
    private RedisTemplate<String, Object> jdkRedisTemplate;


    // 字符串操作
    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 带过期时间的操作
    public void setStringWithExpire(String key, String value, long seconds) {
        stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    // JSON对象操作
    public void setJson(String key, Object value) {
        jsonRedisTemplate.opsForValue().set(key, value);
    }

    public <T> T getJson(String key, Class<T> type) {
        Object value = jsonRedisTemplate.opsForValue().get(key);
        return type.cast(value);
    }

    public void setJsonWithExpire(String key, Object value, long seconds) {
        jsonRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    public void setObject(String key, Object value) {
        jdkRedisTemplate.opsForValue().set(key, value);
    }

    public <T> T getObject(String key, Class<T> type) {
        Object value = jdkRedisTemplate.opsForValue().get(key);
        return type.cast(value);
    }

    public void setObjectWithExpire(String key, Object value, long seconds) {
        jdkRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }


}
