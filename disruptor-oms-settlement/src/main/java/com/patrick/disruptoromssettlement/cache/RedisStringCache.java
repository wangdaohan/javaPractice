package com.patrick.disruptoromssettlement.cache;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@Data
public class RedisStringCache {
    private static RedisStringCache redisStringCache;

    private RedisStringCache(){}

    @Autowired
    private StringRedisTemplate template;

    @Value("${cacheexpire.captcha}")
    private int captchaExpireTime;

    @Value("${cacheexpire.account}")
    private int accountExpireTime;

    @Value("${cacheexpire.order}")
    private int orderExpireTime;


    @PostConstruct
    private void init(){
        redisStringCache = new RedisStringCache();
        redisStringCache.setTemplate(template);
        redisStringCache.setCaptchaExpireTime(captchaExpireTime);
        redisStringCache.setAccountExpireTime(accountExpireTime);
        redisStringCache.setOrderExpireTime(orderExpireTime);
    }

    public static void cache(String key, String value, CacheType cacheType) {
        int expireTime;
        switch (cacheType) {
            case ACCOUNT:
                expireTime = redisStringCache.getAccountExpireTime();
                break;
            case CAPTCHA:
                expireTime = redisStringCache.getCaptchaExpireTime();
                break;
            case ORDER:
            case TRADE:
            case POSI:
                expireTime = redisStringCache.getOrderExpireTime();
                break;
            default:
                expireTime = 10;
        }
        redisStringCache.getTemplate().opsForValue().set(cacheType.type() + key, value, expireTime, TimeUnit.SECONDS);
    }
    public static String get(String key, CacheType cacheType) {
        return redisStringCache.getTemplate().opsForValue().get(cacheType.type() + key);

    }
    public static void remove(String key, CacheType cacheType) {
        redisStringCache.getTemplate().delete(cacheType.type() + key);

    }
}
