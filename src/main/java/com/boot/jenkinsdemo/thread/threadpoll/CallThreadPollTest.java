package com.boot.jenkinsdemo.thread.threadpoll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Simple to Introduction
 * className:  CallThreadPollTest
 *　带返回值的测试类
 * @author: LYL
 * @version: 2019/10/21 16:34
 */
public class CallThreadPollTest {
    public static void main(String[] args) {
        Server server = new Server();
        List<Future<String>> resultList = new ArrayList<Future<String>>();
        for (int i = 1; i <=10 ; i++) {
            String tasknum=String.valueOf(i);
            if(i==5){
                tasknum="五";
            }
            CallTask task=new CallTask(tasknum);
            Future<String> future=server.executorCallTask(task);
            resultList.add(future);
        }

        server.stoppoll();
        for (Future<String> fs : resultList) {
            try {
                System.out.println(fs.get()); // 打印各个线程（任务）执行的结果
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
