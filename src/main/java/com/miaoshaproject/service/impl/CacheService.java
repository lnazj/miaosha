package com.miaoshaproject.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 本次缓存
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-08-28
 */
@Service
public class CacheService implements com.miaoshaproject.service.CacheService {

    private Cache<String,Object> commandCache=null;

    // 利用guava初始化hashmap，设置初始大小，自大存储量，过期时间
    @PostConstruct
    public void init(){
        commandCache= CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    @Override
    public void setCommonCache(String key,Object value){
        commandCache.put(key,value);

    }

    @Override
    public Object getFromCommonCache(String key){
       return commandCache.getIfPresent(key);
    }
}
