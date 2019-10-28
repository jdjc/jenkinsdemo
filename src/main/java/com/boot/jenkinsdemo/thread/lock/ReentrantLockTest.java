package com.boot.jenkinsdemo.thread.lock;

import com.hundsun.jresplus.remoting.impl.annotation.ServiceModule;

import javax.xml.ws.ServiceMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple to Introduction
 * className:  ReentrantLockTest
 *
 * @author: LYL
 * @version: 2019/10/23 14:03
 */
@ServiceModule
public class ReentrantLockTest {
    public static void main(String[] args) {

        ReentrantLockTask task=new ReentrantLockTask();
        ExecutorService service= Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            service.execute(()->{
                task.bustest();
            });
        }
    }
}
