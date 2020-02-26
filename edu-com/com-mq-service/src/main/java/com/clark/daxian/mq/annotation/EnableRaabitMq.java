package com.clark.daxian.mq.annotation;


import com.clark.daxian.mq.config.RabbitConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rabbitConfig相关配置
 * @author 大仙
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RabbitConfig.class)
public @interface EnableRaabitMq {
}
