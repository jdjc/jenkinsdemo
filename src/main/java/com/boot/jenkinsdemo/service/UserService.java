package com.boot.jenkinsdemo.service;

/**
 * Simple to Introduction
 * className:  UserService
 *
 * @author: LYL
 * @version: 2019/10/12 13:27
 */
public interface UserService {
    default String findUserName(String tel){
        return null;
    }
}
