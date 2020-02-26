package com.clark.daxian.wechat.config;


import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.wechat.impl.WeChatComServiceImpl;
import com.clark.daxian.wechat.impl.WeChatPayServiceImpl;
import com.clark.daxian.wechat.properties.WeChatProperties;
import com.clark.daxian.api.wechat.WeChatComService;
import com.clark.daxian.api.wechat.WeChatPayService;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({ WeChatProperties.class})
@ConditionalOnClass(WxPayService.class)
public class WeChatAuthConfig {

    @Autowired
    private WeChatProperties properties;

    private Boolean usePaySandbox = false;

    @Bean
    @ConditionalOnMissingBean
    public WxPayConfig payConfig() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(this.properties.getAppId());
        payConfig.setMchId(this.properties.getMchId());
        payConfig.setMchKey(this.properties.getMchKey());
        payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
        payConfig.setKeyPath(this.properties.getKeyPath());
        payConfig.setUseSandboxEnv(usePaySandbox);
        System.out.println(JSONObject.toJSONString(payConfig));
        return payConfig;
    }

    @Bean
    public WxPayService wxPayService(WxPayConfig payConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }


    @Bean
    public WeChatComService weChatComService(){
        WeChatComServiceImpl weChatComService = new WeChatComServiceImpl();
        weChatComService.setWeChatProperties(properties);
        return weChatComService;
    }

    @Bean
    public WeChatPayService weChatPayService(WxPayConfig payConfig){
        WeChatPayServiceImpl weChatPayService = new WeChatPayServiceImpl();
        weChatPayService.setWxPayProperties(properties);
        weChatPayService.setWxPayService(wxPayService(payConfig));
        return weChatPayService;
    }
}
