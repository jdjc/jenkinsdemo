package com.boot.jenkinsdemo;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class JenkinsdemoApplicationTests {

    /**
     * 数组里取出唯一一个出现奇数次的数
     */
    @Test
    public void contextLoads() {
        int [] arr = {1, 4, 7, 3, 1, 3, 7, 4, 6, 9, 6};
        Map<Integer,Integer> map=new HashMap<Integer,Integer>();
        for (int i = 0; i <arr.length; i++) {
            if(map.containsKey(arr[i])){
                map.remove(arr[i]);
                continue;
            }
            map.put(arr[i],1);
        }

        Iterator iter=map.keySet().iterator();
        while(iter.hasNext()){
            System.out.println(iter.next());
        }
    }

    /**
     * 以单词为单位的字符串反转
     */
    @Test
    public void strReversal(){
        String str="My Name Is Tom";



    }

}
