package com.boot.jenkinsdemo.thread.threadexport;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple to Introduction
 * className:  ThreadFile
 * 多线程并发写同一个文件
 * @author: LYL
 * @version: 2019/10/22 17:54
 */
public class ThreadFile {
    private static ReentrantLock lock=new ReentrantLock();
    public static void writeFile(File file , int num,String filename){
        lock.lock();
        try {
            FileInputStream in = new FileInputStream(file);
            Workbook workBook=null;
            if(file.getName().endsWith("xls")){     //Excel&nbsp;2003
                workBook = new HSSFWorkbook(in);
            }else if(file.getName().endsWith("xlsx")){    // Excel 2007/2010
                workBook = new XSSFWorkbook(in);
            }

            OutputStream out =  new FileOutputStream(filename);
            //获取指定sheet
            int sheetnum=workBook.getNumberOfSheets();
            Sheet sheet=null;
            if(num>=sheetnum){
                sheet= workBook.createSheet();
            }else{
                sheet= workBook.getSheetAt(num);
            }


            for (int j = 0; j <6000 ; j++) {
                Row row=sheet.createRow(j);
                Cell cell= row.createCell(0);
                cell.setCellValue("第"+num+"页第"+j+"行");
            }
            workBook.write(out);
            out.close();
            workBook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }


    }
}
