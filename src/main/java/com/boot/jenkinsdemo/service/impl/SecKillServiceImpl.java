package com.boot.jenkinsdemo.service.impl;

import com.boot.jenkinsdemo.exception.CustomException;
import com.boot.jenkinsdemo.service.SecKillService;
import com.boot.jenkinsdemo.util.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple to Introduction
 * className:  SecKillServiceImpl
 *
 * @author: LYL
 * @version: 2019/10/14 16:46
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    @Autowired
    private RedisLock redisLock;

    private static final int TIMOUT = 10 * 1000; //超时时间 10秒

    /**
     * 中秋活动 秒杀月饼 限量100000
     */
    static Map<String, Integer> products;
    static Map<String, Integer> stock;
    static Map<String, String> orders;

    static {
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("abc123456", 10000);
        stock.put("abc123456", 10000);
    }

    private String queryMap(String productId) {
        return "中秋活动，月饼特价，限量份"
                + products.get(productId)
                + " 还剩：" + stock.get(productId) + " 份"
                + " 该商品成功下单用户数目："
                + orders.size() + " 人";
    }

    @Override
    public String querySecKillProductInfo(String productId) {
        return queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId) {
        long time = System.currentTimeMillis() + TIMOUT;
        //加锁
        if(!redisLock.lock(productId,String.valueOf(time))){
            throw new CustomException("商品销售火爆,一会再试"+time);
        }
        //获取库存
        Integer stockNum=stock.get(productId);
        if(stockNum==0){
            throw new CustomException("商品已售罄,谢谢参与");
        }
        orders.put(String.valueOf(System.currentTimeMillis()*10),productId);
        stockNum=stockNum-1;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stock.put(productId, stockNum);
        redisLock.unlock(productId,String.valueOf(time));

    }
}
