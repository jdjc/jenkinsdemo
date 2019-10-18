package com.boot.jenkinsdemo.config;


import com.boot.jenkinsdemo.common.GenericFastJson2JsonRedisSerializer;
import com.boot.jenkinsdemo.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis配置类
 *
 * @author: LYL
 * @version: 2019/10/12 10:07
 */
@Configuration
@EnableCaching
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "spring")
//@PropertySource("classpath:redis.properties")
@Slf4j
@Data
public class RedisConfig {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    private String host;

    private int port;

    private String password;

    private int timeout;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisPool redisPoolFactory() {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMaxWaitMillis(maxWait);
            jedisPoolConfig.setMaxTotal(maxActive);
            jedisPoolConfig.setMinIdle(minIdle);
            // JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
            String pwd = StringUtil.isBlank(password) ? null : password;
            JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, pwd);
            logger.info("初始化Redis连接池JedisPool成功!地址: " + host + ":" + port);
            return jedisPool;
        } catch (Exception e) {
            logger.error("初始化Redis连接池JedisPool异常:" + e.getMessage());
        }
        return null;
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate redisLoginTemplate(LettuceConnectionFactory factory){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

    private StringRedisTemplate getRedisTemplate(){
        StringRedisTemplate template = new StringRedisTemplate();
        template.setValueSerializer(new GenericFastJson2JsonRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    /**
     * 在springboot中使用spring-session的时候，
     * 在不同的域名下面需要配置cookie主域否则session共享不生效
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        //cookie名字
        defaultCookieSerializer.setCookieName("sessionId");
        //不同子域时设置
        //defaultCookieSerializer.setDomainName("xxx.com");
        //设置各web应用返回的cookiePath一致
        defaultCookieSerializer.setCookiePath("/");
        return defaultCookieSerializer;
    }
}
