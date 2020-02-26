package com.clark.daxian.gateway;


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
public class GatewayApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
