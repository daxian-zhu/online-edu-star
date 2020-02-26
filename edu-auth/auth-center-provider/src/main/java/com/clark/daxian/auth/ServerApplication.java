package com.clark.daxian.auth;

import com.clark.daxian.mybatis.annotation.EnableShardingJdbc;
import com.clark.daxian.wechat.annotation.EnableWeChat;
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
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
