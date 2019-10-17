package com.boot.jenkinsdemo.service.impl;

import com.boot.jenkinsdemo.annotation.AnnLog;
import com.boot.jenkinsdemo.exception.CustomException;
import com.boot.jenkinsdemo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 *
 * @author: LYL
 * @version: 2019/10/12 13:28
 */
@Service
public class UserServiceImpl implements UserService {

    @AnnLog(detail = "testlog,通过手机号{{tel}}获取用户名")
    @Override
    public String findUserName(String tel) {
        System.out.println("tel:" + tel);
        try{
            Long.parseLong(tel);
        }catch (Exception e){
            throw new CustomException("异常了"+e.getMessage());
        }

        return "成功返回：　admin";
    }
}
