package com.boot.jenkinsdemo.thread.threadpoll;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Simple to Introduction
 * className:  Task
 *
 * @author: LYL
 * @version: 2019/10/21 10:19
 */
public class Task implements Runnable{
    private String name;
    private CountDownLatch countDownLatch;
    public Task(String name,CountDownLatch countDownLatch){
        this.name=name;
        this.countDownLatch=countDownLatch;
    }
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void run() {

        Date startdate=new Date(System.currentTimeMillis());
        String starttiem=sdf.format(startdate);
        System.out.println("*******************"+name+"开始执行"+starttiem+"**************");
        try {
            long sleeptime=(long) (Math.random()*10);
            System.out.println(name+"睡眠"+sleeptime);
            TimeUnit.SECONDS.sleep(sleeptime);
            Date enddate=new Date(System.currentTimeMillis());
           //System.out.println(sdf.format("执行完成"+sdf.format(enddate)));
            //System.out.println("\t时间："+(enddate.getTime()-startdate.getTime()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //countDownLatch.countDown();
            //System.out.println("*******************"+name+"执行结束**********************还剩下："+countDownLatch.getCount());
            System.out.println("*******************"+name+"执行结束**********************");
        }


    }
}
