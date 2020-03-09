package com.clark.daxian.im;

import com.clark.daxian.im.netty.NettyServer;
import com.clark.daxian.mq.annotation.EnableRabbitMq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * IM服务启动类
 */
@SpringBootApplication
@EnableRabbitMq
@EnableFeignClients
public class ImApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ImApplication.class);
    }

    @Autowired
    private NettyServer nettyServer;
    /**
     * 项目启动之后启动netty服务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        nettyServer.start();
    }
}
