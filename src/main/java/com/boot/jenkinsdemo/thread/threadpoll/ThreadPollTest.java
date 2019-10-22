package com.boot.jenkinsdemo.thread.threadpoll;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Simple to Introduction
 * className:  ThreadPollTest
 * 线程池测试
 * @author: LYL
 * @version: 2019/10/21 10:12
 */
public class ThreadPollTest {
    public static void main(String[] args) {
        Server server=new Server();
        final int nThreads=10;
        CountDownLatch countDownLatch=new CountDownLatch(nThreads);
        for (int i = 1; i <=10 ; i++) {
            Task task=new Task("任务"+i,countDownLatch);
            server.executorTask(task);
        }
        //方案一　使用CountDownLatch结束
//        try {
//            countDownLatch.await();
//            System.out.println("执行完了");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //方案二  使用isTerminated结束
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <=5 ; i++) {
            Task task=new Task("新任务"+i,countDownLatch);
            server.executorTask(task);
        }
        server.stoppoll();



    }
}
