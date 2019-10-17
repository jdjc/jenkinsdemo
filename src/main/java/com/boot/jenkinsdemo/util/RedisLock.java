package com.boot.jenkinsdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Simple to Introduction
 * className:  RedisLock
 *
 * @author: LYL
 * @version: 2019/10/14 14:16
 */
@Component
@Slf4j
public class RedisLock {
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    public boolean lock(String key,String value){
        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            return true;
        }
        //currentValue=A 这两个线程的value都是B 其中一个线程拿到锁
        String currentValue=redisTemplate.opsForValue().get(key);
        if(StringUtil.isNotBlank(currentValue) && Long.parseLong(currentValue)< System.currentTimeMillis()){
            //获取上一个锁的时间
            String oldVlaue=redisTemplate.opsForValue().getAndSet(key,value);
            if(StringUtil.isNotBlank(oldVlaue) && oldVlaue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key,String value){
        String currentVaule = redisTemplate.opsForValue().get(key);
        try {
            if (StringUtil.isNotBlank(currentVaule)  && currentVaule.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】解锁异常，{}" , e);
        }
    }
}
