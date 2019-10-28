package com.boot.jenkinsdemo.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false，作为阻塞式锁使用
     * @param key 锁键
     * @param value 被谁锁定
     * @param timeout 尝试获取锁时长，建议传递500,结合实践单位，则可表示500毫秒
     * @param unit，建议传递TimeUnit.MILLISECONDS
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String key , String value , long timeout , TimeUnit unit) throws InterruptedException {
        //纳秒
        long begin = System.nanoTime();
        do {
            if (redisTemplate.opsForValue().setIfAbsent(key,value)) {
                redisTemplate.expire(key,timeout,TimeUnit.SECONDS);
                log.info("成功获取{}的锁,设置锁过期时间为30秒",key);
                return Boolean.TRUE;
            } else {
                // 存在锁 ，但可能获取不到，原因是获取的一刹那间
                  String desc = redisTemplate.opsForValue().get(key);
                  log.info("{}等待获取{}的锁,正被{}锁定",Thread.currentThread().getName(),key,desc);
            } if (timeout == 0) {
                break;
            }
            //在其睡眠的期间，锁可能被解，也可能又被他人占用，但会尝试继续获取锁直到指定的时间
            Thread.sleep(100);
        } while ((System.nanoTime() - begin) < unit.toNanos(timeout));
        //因超时没有获得锁
        return Boolean.FALSE;
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
