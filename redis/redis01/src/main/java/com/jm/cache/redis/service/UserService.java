package com.jm.cache.redis.service;

import com.alibaba.fastjson.JSONObject;
import com.jm.cache.redis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    JedisPool jedisPool;

    /**
     * 根据ID查询用户信息 (redis缓存，用户信息以json字符串格式存在(序列化))
     */
    public User findUserById(String userId) {
        User user = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();

            // 1、 查询 redis 是否有数据 -- hash 方式
            Map<String, String> result = jedis.hgetAll(userId);
            if(result != null && "".equals(result)){
                // map 对象转换为User
                user = new User();
                user.setAge(Integer.valueOf(result.get("age")));
                user.setImg(result.get("img"));
                user.setUname(result.get("uname"));
                user.setUid(result.get("uid"));
                return user;
            }

            //2、查询数据库
            String sql = "select * from tb_user_base where uid=?";
            user = jdbcTemplate.queryForObject(sql, new String[]{userId}, new BeanPropertyRowMapper<>(User.class));

            // 3、将数据放入redis
            String userJsonStr = JSONObject.toJSONString(user);
            HashMap<String, String> userInfo = new HashMap<>();
            userInfo.put("age", String.valueOf(user.getAge()));
            userInfo.put( "uname", String.valueOf(user.getUname()));
            userInfo.put( "uid", String.valueOf(user.getUid()));
            userInfo.put( "img", String.valueOf(user.getImg()));
            jedis.hmset(userId, userInfo);

        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return user;
    }


    /**
     * 根据ID查询用户名称
     */
    public String findUserNameById(String userId) {
        String uname = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 1、查询 redis 是否有数据
            uname = jedis.hget(userId, "uname");
            if(uname != null && "".equals(uname)){
                return uname;  // 命中缓存
            }

            //2、查询数据库
            String sql = "select uname from tb_user_base where uid=?";
            uname = jdbcTemplate.queryForObject(sql, new String[]{userId}, String.class);

            // 3、将数据放入redis
            jedis.hset(userId, "uname", uname);

        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return uname;
    }

    // 导致不一致
    public synchronized void update() {
        // 修改缓存中的内容
        // redis.set

        // 修改数据库的内容
        // jdbcTemplate.update()

    }
}
