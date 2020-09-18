package com.jm.cache.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import redis.clients.jedis.JedisPool;

// 用这个类 代替了xml文件
@Configuration //
public class AppConfig {


    // 创建对象，spring托管 <bean ...
    @Bean
    public JedisPool jedisPool() {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        return jedisPool;
    }

   /* @Bean
    public RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        return redisTemplate;
    }*/
}
