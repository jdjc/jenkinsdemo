package com.boot.jenkinsdemo.thread.threadpoll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Simple to Introduction
 * className:  ThreadPool
 *
 * @author: LYL
 * @version: 2019/10/22 14:25
 */
public class ThreadPool {
    private static final int CORE_SIZE =5;
    private static final int MAX_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 3;
    private static final int QUEUE_SIZE = 500;
    private static ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,KEEP_ALIVE_TIME, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());
    public static ThreadPoolExecutor getThreadPool(){
        return threadPoolExecutor;
    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor=ThreadPool.getThreadPool();
        List<Future<Long>> list=new ArrayList<Future<Long>>();
        for (int i = 1; i <=30; i++) {
            Future task=threadPoolExecutor.submit(()->{
                Thread.sleep(1000);
                return Arrays.asList(1L,2L);
            });
            System.out.println(threadPoolExecutor.getActiveCount());
            list.add(task);
        }



        for(Future<Long> f:list){
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
