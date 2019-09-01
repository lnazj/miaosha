package com.miaoshaproject.service;

/**
 * 封装本地缓存操作类
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-08-28
 */

public interface CacheService {
    //存方法
    void setCommonCache(String key,Object value);

    // 取方法
    Object getFromCommonCache(String key);

}
