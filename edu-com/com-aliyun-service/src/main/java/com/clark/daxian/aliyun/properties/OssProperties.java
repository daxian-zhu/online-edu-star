package com.clark.daxian.aliyun.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * oss配置类
 * @author 大仙
 */
@Data
@ConfigurationProperties(prefix = "edu.aliyun.oss")
public class OssProperties implements Serializable {

    /**
     * 站点
     */
    private String point;
    /**
     * keyId
     */
    private String key;
    /**
     * key密码
     */
    private String secret;
    /**
     * bucket名称
     */
    private String buckName;
    /**
     * 访问路径
     */
    private String baseUrl;
    /**
     * 角色管理获得
     */
    private String roleArn;
    /**
     * session名称
     */
    private String roleSessionName;
}
