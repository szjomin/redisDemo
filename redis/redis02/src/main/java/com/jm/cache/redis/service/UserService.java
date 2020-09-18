package com.jm.cache.redis.service;

import com.alibaba.fastjson.JSONObject;
import com.jm.cache.redis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate; // spring提供jdbc一个工具（mybastis类似）

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 根据ID查询用户信息 (redis缓存，用户信息以json字符串格式存在(序列化))
     */
    public User findUserById(String userId) {
        // 1、read cache
        User user = null;
        Object result = (User)redisTemplate.opsForValue().get("userId");
        if(user != null){
            return (User)result;
        }

        // 2、查询数据库
        String sql = "select * from tb_user_base where uid=?";
        user = jdbcTemplate.queryForObject(sql, new String[]{userId}, new BeanPropertyRowMapper<>(User.class));

        // set cache
        redisTemplate.opsForValue().set(userId, user);
        return user;
    }

}
