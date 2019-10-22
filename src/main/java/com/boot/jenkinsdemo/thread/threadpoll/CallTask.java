package com.boot.jenkinsdemo.thread.threadpoll;

import com.boot.jenkinsdemo.exception.CustomException;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Simple to Introduction
 * className:  CallTask
 * 带返回值的线程
 * @author: LYL
 * @version: 2019/10/21 16:28
 */
public class CallTask implements Callable<String> {
    private String name;
    public CallTask(String name){
        this.name=name;
    }
    @Override
    public String call() throws Exception {
        TimeUnit.SECONDS.sleep((long) (Math.random()*10));
        try{
            Integer.valueOf(name);
        }catch (Exception e){
            throw new CustomException("执行出错"+e.getMessage());
        }

        return "任务"+name+"正常执行";
    }
}
