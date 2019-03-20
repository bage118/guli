package com.guli.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
//两种方法扫描
//@ComponentScan("com.guli.edu")
//@ComponentScan("com.guli.common")
@ComponentScan(basePackages = {"com.guli.edu","com.guli.common"} )
@EnableEurekaClient
@EnableFeignClients
public class EduApplication
{
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class,args);
    }
}
