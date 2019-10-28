package com.boot.jenkinsdemo.thread.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple to Introduction
 * className:  ReentrantLockTask
 *
 * @author: LYL
 * @version: 2019/10/23 14:04
 */
@Slf4j
public class ReentrantLockTask {
    private Lock lock=new ReentrantLock();
    public ReentrantLockTask(){

    }

    public void bustest(){
        log.info("{}进入方法",Thread.currentThread().getName());
        lock.lock();
        log.info("\t{}获取锁",Thread.currentThread().getName());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("\t\t{}释放锁",Thread.currentThread().getName());
        lock.unlock();

    }

}
