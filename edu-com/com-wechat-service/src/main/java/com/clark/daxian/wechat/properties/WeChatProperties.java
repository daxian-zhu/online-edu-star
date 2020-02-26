package com.clark.daxian.wechat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ConfigurationProperties(prefix = "edu.wechat")
public class WeChatProperties implements Serializable {
    /**
     * 设置微信公众号的appid
     */
    private String appId;
    /**
     * 微信公众号Secret
     */
    private String appSecret;
    /**
     * 使用开启token缓存
     */
    private Boolean startTokenCache = false;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付商户密钥
     */
    private String mchKey;

    /**
     * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除
     */
    @Deprecated
    private String subAppId;

    /**
     * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除
     */
    @Deprecated
    private String subMchId;

    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    private String keyPath;
}
