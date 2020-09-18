package com.jm.cache.redis.controller;

import com.jm.cache.redis.pojo.User;
import com.jm.cache.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 查看所有信息
     */
    @RequestMapping("/findUserById")
    public User findUserById(String userId) {
        return userService.findUserById(userId);
    }

    /**
     * 查看姓名
     */
    /*@RequestMapping("/findUserNameById")
    public String findUserNameById(String userId) {
        return userService.findUserNameById(userId);
    }*/


    @RequestMapping("/getMemoryString")
    public String getMemoryString() {
        return "hello"; // 无数据库 无接口调用
    }

}
