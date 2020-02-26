package com.clark.daxian.aliyun.config;

import com.clark.daxian.aliyun.impl.VodServiceImpl;
import com.clark.daxian.aliyun.properties.VodProperties;
import com.clark.daxian.api.aliyun.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({ VodProperties.class})
public class VodAutoConfig {

    @Autowired
    private VodProperties vodProperties;

    /**
     * 阿里云视频点播业务
     * @return
     */
    @Bean
    VodService vodService(){
        VodServiceImpl vodService = new VodServiceImpl();
        vodService.setVodProperties(vodProperties);
        return vodService;
    }
}
