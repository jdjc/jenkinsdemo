package com.boot.jenkinsdemo.controller;
import com.boot.jenkinsdemo.common.ResponseBean;
import com.boot.jenkinsdemo.exception.CustomException;
import com.boot.jenkinsdemo.service.UserService;
import com.boot.jenkinsdemo.service.impl.SecKillServiceImpl;
import com.boot.jenkinsdemo.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple to Introduction
 * className:  IndexController
 *
 * @author: LYL
 * @version: 2019/10/11 9:32
 */
@RestController
@RequestMapping(value = "/redis")
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecKillServiceImpl secKillService;

   // @RequestMapping(value = "initString",method = RequestMethod.GET)
    public String initStringData() {
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                JedisUtil.setObject("key-" + i, "val-" + i, 5 + i);
            } else {
                JedisUtil.setObject("key-" + i, "val-" + i, 5);
            }
        }
        return "success";
    }

    /**
     * 查询秒杀活动特价商品的信息
     * @param productId
     * @return
     */
    @GetMapping("/query/{productId}")
    public String query(@PathVariable String productId)throws Exception{
        return secKillService.querySecKillProductInfo(productId);
    }

    /**
     * 秒杀，没有抢到获得"哎呦喂,xxxxx",抢到了会返回剩余的库存量
     * @param productId
     * @return
     * @throws Exception
     */
    @GetMapping("/order/{productId}")
    public String skill(@PathVariable String productId){
        System.out.println("产品："+productId);
        try{
            secKillService.orderProductMockDiffUser(productId);
        }catch (CustomException e){
            System.out.println("\t\t"+e.getMessage());
        }

        return secKillService.querySecKillProductInfo(productId);
    }


    @RequestMapping(value = "findUserName",method = RequestMethod.GET)
    public String findUserName(HttpServletRequest request) {
        String tel=request.getParameter("tel");
        if(null==tel){
            tel="13001277891";
        }
        return  userService.findUserName(tel);
    }

    /**
     * string 数据结构
     * @return
     */
    @RequestMapping(value = "string",method = RequestMethod.GET)
    public ResponseBean getRedis() {
        System.out.println("测试redis");
        // String类型

        //list类型


        //hash类型


        //set类型


        //zset类型
        Map<String, Object> result = new HashMap<String, Object>(16);
        //result.put("count", selectPage.getTotal());
        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            keys.add("key-" + i + ":" + JedisUtil.getObject("key-" + i) + ",time:" + JedisUtil.ttl("key-" + i));
        }
        result.put("data", keys);

       // userService.getClass().getResource("");

        Method[] methods= userService.getClass().getMethods();
        for(Method m:methods){
            System.out.println(m.getName());
            for(Parameter p:m.getParameters()){
                System.out.println("\t"+p.getName());
            }
        }

        return new ResponseBean(HttpStatus.OK.value(), "查询成功", result);
    }

    @RequestMapping(value="/setsession",method = RequestMethod.GET)
    public String setSession(HttpServletRequest request){
        Map<String,Object> map = new HashMap();
        map.put("name","我是超级管理员"+System.currentTimeMillis());
        map.put("account","admin");
        request.getSession().setAttribute("userSession",map);
        String sessionId = request.getSession().getId();
        System.out.println("sessionid:"+sessionId);
        return sessionId;
    }

    @RequestMapping(value="/getsession",method = RequestMethod.GET)
    public Map<String,Object> getSession(HttpServletRequest request){
        String sessionId = request.getSession().getId();
        System.out.println("get sessionID:"+sessionId);
        System.out.println("-=============="+redisTemplate.opsForValue().get("userSession"));
        Object obj = request.getSession().getAttribute("userSession");
        Map<String,Object> map = new HashMap();
        map.put("sessionId",sessionId);
        map.put("user",obj);
        return map;
    }

}
