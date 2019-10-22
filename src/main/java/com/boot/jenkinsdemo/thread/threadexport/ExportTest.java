package com.boot.jenkinsdemo.thread.threadexport;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Simple to Introduction
 * className:  ExportTest
 * 模拟导出
 * @author: LYL
 * @version: 2019/10/22 15:42
 */
public class ExportTest {
    public static void main(String[] args) {
        ExportThreadPoll exportThreadPoll=new ExportThreadPoll(5);
        String filename="E:\\test导出.xlsx";
        File file=new File(filename);
        try {
            if(null==file || !file.exists()){
                file.createNewFile();
                System.out.println("文件已创建");
            }else{
//                file.delete();
//                System.out.println("文件已删除");
//                file=new File(filename);
//                file.createNewFile();
//                System.out.println("文件重新创建");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       Workbook workBook = null;
        try {

            List<Future<Integer>> futureList=new ArrayList<Future<Integer>>();
            CountDownLatch cdl=new CountDownLatch(10);
            for (int i = 0; i < 10; i++) {
                ExportTask task=new ExportTask(i,filename,workBook,cdl,file);
                Future<Integer> future = exportThreadPoll.submit(task,i);
                futureList.add(future);
            }
            cdl.await();

            for (Future f:futureList){
                System.out.println(f.get()+"完成");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        exportThreadPoll.shutdown();


    }

}
