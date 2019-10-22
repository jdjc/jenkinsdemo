package com.boot.jenkinsdemo.thread.threadpoll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Simple to Introduction
 * className:  CallThreadPollTest
 *　带返回值的测试类
 * @author: LYL
 * @version: 2019/10/21 16:34
 */
public class CallFixedThreadPollTest {
    public static void main(String[] args) {
//        FixedThreadPool server = new FixedThreadPool();
//        List<Future<String>> resultList = new ArrayList<Future<String>>();
//        for (int i = 1; i <=15 ; i++) {
//            String tasknum=String.valueOf(i);
//            if(i==5){
//                tasknum="五";
//            }
//            CallTask task=new CallTask(tasknum);
//            Future<String> future=server.submit(task);
//            resultList.add(future);
//        }
//        server.stoppoll();
//        for (Future<String> fs : resultList) {
//            try {
//                System.out.println(fs.get()); // 打印各个线程（任务）执行的结果
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                System.out.println(e.getMessage());
//            }
//        }

        //定长线程池
        ExecutorService eservice= Executors.newFixedThreadPool(3);
        for (int i = 1; i <=15 ; i++) {
            String tasknum=String.valueOf(i);
            Task task=new Task("任务"+tasknum,null);
           // eservice.execute(task);
            eservice.execute(()->{
                try {
                    System.out.println("任务执行==>"+Thread.currentThread().getName());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("任务执行==>"+Thread.currentThread().getName()+"执行完成");
                }
            });
        }

        try {
            Thread.sleep(20000);
            eservice.execute(()->{
                System.out.println("最新任务");
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        eservice.shutdown();
        while(true){
            if(eservice.isTerminated()){
                System.out.println("执行完毕");
                //eservice.execute(()-> System.out.println("333"));
                break;
            }
        }
    }
}
