package com.boot.jenkinsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
public class JenkinsdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JenkinsdemoApplication.class, args);
    }

}
