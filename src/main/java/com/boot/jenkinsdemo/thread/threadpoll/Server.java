package com.boot.jenkinsdemo.thread.threadpoll;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Simple to Introduction
 * className:  Server
 *
 * @author: LYL
 * @version: 2019/10/21 10:19
 */
public class Server {
    private ThreadPoolExecutor executor;
    public Server(){
        executor= (ThreadPoolExecutor)Executors.newCachedThreadPool();
    }

    /**
     * runnable接口
     * @param thread
     */
    public void executorTask(Runnable thread){
        System.out.println("加入一个任务");
        executor.execute(thread);
        System.out.println("大小："+executor.getPoolSize());
        System.out.println("运行大小："+executor.getActiveCount());
        System.out.println("已执行的任务："+executor.getCompletedTaskCount());
    }

    /**
     * 带返回值　Callable
     * @param task
     * @return
     */
    public Future<String> executorCallTask(Callable task){
        System.out.println("加入一个带返回值的任务");
        return executor.submit(task);
    }


    public void stoppoll(){
        executor.shutdown();
        while (true){
            //注意除非首先调用shutdown或shutdownNow，否则isTerminated永不为true
            if(executor.isTerminated()){ //若关闭后所有任务都已完成，则返回true
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
