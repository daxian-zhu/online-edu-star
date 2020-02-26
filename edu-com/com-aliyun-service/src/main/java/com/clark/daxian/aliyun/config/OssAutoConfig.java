package com.clark.daxian.aliyun.config;

import com.clark.daxian.aliyun.impl.OssServiceImpl;
import com.clark.daxian.aliyun.properties.OssProperties;
import com.clark.daxian.api.aliyun.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * OSS config
 * @author 大仙
 */
@EnableConfigurationProperties({ OssProperties.class})
public class OssAutoConfig {

    @Autowired
    OssProperties ossProperties;

    /**
     * 实例化OSS
     * @return
     */
    @Bean
    OssService ossService(){
        OssServiceImpl ossService = new OssServiceImpl();
        ossService.setOssProperties(ossProperties);
        return ossService;
    }

}
