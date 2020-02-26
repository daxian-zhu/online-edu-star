package com.clark.daxian.user;

import com.clark.daxian.mybatis.annotation.EnableShardingJdbc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 * @author 大仙
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableShardingJdbc
public class UserApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserApplication.class, args);
    }
}
