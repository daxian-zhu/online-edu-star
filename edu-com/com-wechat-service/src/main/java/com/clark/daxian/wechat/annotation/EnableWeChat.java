package com.clark.daxian.wechat.annotation;


import com.clark.daxian.wechat.config.WeChatAuthConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启WeChat配置
 * @author 大仙
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(WeChatAuthConfig.class)
public @interface EnableWeChat {
}
