package com.patrick.disruptoromssettlement.api;

import com.patrick.disruptoromssettlement.util.DbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/helloRedis")
    public String helloRedis(){
        stringRedisTemplate.opsForValue().set("test:hello","redis");
        return stringRedisTemplate.opsForValue().get("test:hello");
    }


    @RequestMapping("/hello")
    public String hello(){
        return "" + DbUtil.getId();
    }


}
