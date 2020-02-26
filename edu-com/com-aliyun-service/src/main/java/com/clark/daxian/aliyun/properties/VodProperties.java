package com.clark.daxian.aliyun.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 视频配置类
 * @author 大仙
 */
@Data
@ConfigurationProperties(prefix = "edu.aliyun.vod")
public class VodProperties implements Serializable {
    /**
     * key：单独权限
     */
    private String key;
    /**
     * secret: 单独权限
     */
    private String secret;
    /**
     * 区域
     */
    private String region;
    /**
     * 用户ID
     */
    private String uid;
    /**
     * 图片前缀
     */
    private String coverUrl;
    /**
     * 模板组
     */
    private String templateMode;

}
