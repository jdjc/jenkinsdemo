package com.boot.jenkinsdemo.thread.threadexport;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * Simple to Introduction
 * className:  ExportTask
 *
 * @author: LYL
 * @version: 2019/10/22 15:51
 */
public class ExportTask implements Callable<Integer> {
    private Integer num;
    private String filename;
    private CountDownLatch cdl;
    private File file;
    public ExportTask(Integer num, String filename, Workbook workBook, CountDownLatch cdl,File file){
        this.num = num;
        this.filename=filename;
        this.cdl=cdl;
        this.file=file;
    }
    @Override
    public Integer call() throws Exception {
        System.out.println("第"+num+"页开始执行");
        ThreadFile.writeFile(file,num,filename);
        System.out.println("\t第"+num+"页写入完成");
        cdl.countDown();
        return num;
    }

}
