package com.boot.jenkinsdemo.controller;
import com.alibaba.fastjson.JSON;
import com.boot.jenkinsdemo.common.ResponseBean;
import com.boot.jenkinsdemo.exception.CustomException;
import com.boot.jenkinsdemo.service.UserService;
import com.boot.jenkinsdemo.service.impl.SecKillServiceImpl;
import com.boot.jenkinsdemo.util.JedisUtil;
import com.boot.jenkinsdemo.util.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Simple to Introduction
 * className:  IndexController
 *
 * @author: LYL
 * @version: 2019/10/11 9:32
 */
@RestController
@RequestMapping(value = "/redis")
@Slf4j
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecKillServiceImpl secKillService;

    @Autowired
    private RedisLock redisLock;

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
     * 测试注册手机号记录验证码次数
     * @param mobile
     */
    @RequestMapping(value = "regMobileCheck",method = RequestMethod.GET)
    public void checkRegMobileCount(@RequestParam(value = "mobile")String mobile){
        String key="CHECK_REG_MOBILE_COUNT"+mobile;
        long msgcount=redisTemplate.opsForValue().increment(key,1);
        if(msgcount==1){
            Calendar curDate = Calendar.getInstance();
            Calendar nextDayDate = new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE)+1, 0, 0, 0);
            long remainTime = (nextDayDate.getTimeInMillis() - curDate.getTimeInMillis())/1000;
            System.out.println(remainTime);
            redisTemplate.expire(key,60, TimeUnit.SECONDS);
        }else{
           String count = redisTemplate.opsForValue().get(key);
           log.info("手机号{}已经发了{}条",mobile,count);
        }
    }




    /**
     * 测试redis分布式锁
     * @param loanid
     */
    @RequestMapping(value = "trylock",method = RequestMethod.GET)
    public void checkTrylock(@RequestParam(value = "loanid")String loanid){

        ExecutorService executorService=Executors.newFixedThreadPool(5);
        for (int i = 0; i < 8; i++) {
            executorService.execute(()->{
                log.info(Thread.currentThread().getName()+"企图获取{}的锁",loanid);
                try {
                    redisLock.tryLock(loanid,loanid,10,TimeUnit.SECONDS);
                    log.info("\t{}获取到{}的锁",Thread.currentThread().getName(),loanid);
                    Thread.sleep(1000);
                    redisLock.unlock(loanid,loanid);
                    log.info("\t\t{}正常释放{}的锁",Thread.currentThread().getName(),loanid);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    log.info("{}finally释放{}的锁",Thread.currentThread().getName(),loanid);
                    redisLock.unlock(loanid,loanid);
                }
            });
        }

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
    public String setSession(HttpServletRequest request,@RequestParam(value = "username")String username,HttpSession
            session){
        Object obj=session.getAttribute("username");
        if(obj==null){
            System.out.println("不存在session,加入username:"+username);
            Map<String,Object> map = new HashMap();
            map.put("name",username+"_"+System.currentTimeMillis());
            map.put("account",username);
            map.put("sessionid",session.getId());
            session.setAttribute(username,map);
        }
        System.out.println(username+"_sessionid:"+session.getId());
        return username+"_sessionid:"+session.getId();
    }

    @RequestMapping(value="/getsession",method = RequestMethod.GET)
    public Map<String,Object> getSession(HttpServletRequest request,HttpSession
            session){
        String username=request.getParameter("username");
        String sessionId = request.getSession().getId();
        System.out.println("获取====>"+username+"_sessionID:"+sessionId);
        Object obj = request.getSession().getAttribute(username);
        if(obj!=null){
            Cookie[] cookies=request.getCookies();
            if(cookies!=null && cookies.length>0){
                for (Cookie c:cookies){
                    System.out.println("\t\t\t"+c.getName()+"."+c.getValue());
                }
            }
        }
        Map<String,Object> map = new HashMap();
        map.put("sessionId",sessionId);
        map.put("user",obj);
        return map;
    }



}
