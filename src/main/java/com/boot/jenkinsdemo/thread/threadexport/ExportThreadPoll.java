package com.boot.jenkinsdemo.thread.threadexport;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Simple to Introduction
 * className:  ExportThreadPoll
 *
 * @author: LYL
 * @version: 2019/10/22 15:43
 */
public class ExportThreadPoll {
    private ExecutorService executor;
    public ExportThreadPoll(int casesize){
        executor= Executors.newFixedThreadPool(casesize);
    }

    public Future<Integer> submit(Callable task,Integer index){
        System.out.println("加入任务"+index);
        Future<Integer> future = executor.submit(task);
        return future;
    }

    public void shutdown(){
        executor.shutdown();
        while(true){
            if(executor.isTerminated()){
                System.out.println("写入完成");
                break;
            }
        }
    }
}
