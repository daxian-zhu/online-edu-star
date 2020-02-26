package com.clark.daxian.aliyun.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 短信配置类
 * @author 大仙
 */
@Data
@ConfigurationProperties(prefix = "edu.aliyun.sms")
public class SmsProperties implements Serializable {
    /**
     * 短信API产品名称（短信产品名固定，无需修改）
     */
    private String  product;
    /**
     * 短信API产品域名（接口地址固定，无需修改）
     */
    private String domain;
    /**
     * 签名名称
     */
    private String  signName;
    /**
     * 断点
     */
    private String endpoint;
    /**
     * key：单独授权
     */
    private String key;
    /**
     * secret: 单独授权
     */
    private String secret;
}
