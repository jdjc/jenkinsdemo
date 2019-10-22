package com.boot.jenkinsdemo.thread.threadpoll;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Simple to Introduction
 * className:  FixedThreadPool
 *
 * @author: LYL
 * @version: 2019/10/21 16:57
 */
public class FixedThreadPool {
    private ExecutorService service;
    public  FixedThreadPool(){
        service= Executors.newFixedThreadPool(3);
    }

    /**
     * 带返回值
     * @param task
     * @return
     */
    public Future<String> submit(Callable task){
        System.out.println("加入一个任务");
        Future<String> future= service.submit(task);
        return future;
    }

    /**
     * 不带返回值
     * @param task
     * @return
     */
    public void executeTask(Runnable task){
        System.out.println("加入一个Runnable任务");
        service.execute(task);
    }

    public void stoppoll(){
        service.shutdown();
        while (true){
            //注意除非首先调用shutdown或shutdownNow，否则isTerminated永不为true
            if(service.isTerminated()){ //若关闭后所有任务都已完成，则返回true
                System.out.println("关闭线程池");
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
