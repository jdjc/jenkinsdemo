package com.boot.jenkinsdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Simple to Introduction
 * className:  SessionConfig
 *
 * @author: LYL
 * @version: 2019/10/15 15:40
 */

@Configuration
//@EnableCaching//开启缓存注解
//RedisFlushMode有两个参数：ON_SAVE（表示在response commit前刷新缓存），IMMEDIATE（表示只要有更新，就刷新缓存）
@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 60,redisFlushMode = RedisFlushMode.ON_SAVE, redisNamespace = "testsession") //SpringSession的过期时间（单位：秒）
public class SessionConfig {
//    @Bean
//    public static ConfigureRedisAction configureRedisAction() {
//        //让springSession不再执行config命令
//        return ConfigureRedisAction.NO_OP;
//    }
}
