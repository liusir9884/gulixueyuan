package com.liu.eduservice;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.liu"})
@EnableFeignClients
@MapperScan("com.liu.eduservice.mapper")
public class CmtApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmtApplication.class,args);
    }
}
