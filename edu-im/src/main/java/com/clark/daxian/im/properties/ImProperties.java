package com.clark.daxian.im.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 相关配置文件
 * @author 大仙
 */
@Data
@ConfigurationProperties(prefix = "edu.im")
@Component
public class ImProperties {

    private Integer port;

    private int maxFrameLength;

    private int IdleReadTimeout;

    private int IdleWriteTimeout;

    private int IdleAllTimeout;

    private String websocketUrl;

}
