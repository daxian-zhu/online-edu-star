package com.clark.daxian.aliyun.config;


import com.clark.daxian.aliyun.impl.SmsServiceImpl;
import com.clark.daxian.aliyun.properties.SmsProperties;
import com.clark.daxian.api.aliyun.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({ SmsProperties.class})
public class SmsAutoConfig {

    @Autowired
    SmsProperties smsProperties;

    /**
     * 实例化OSS
     * @return
     */
    @Bean
    SmsService smsService(){
        SmsServiceImpl smsService = new SmsServiceImpl();
        smsService.setSmsProperties(smsProperties);
        return smsService;
    }
}
