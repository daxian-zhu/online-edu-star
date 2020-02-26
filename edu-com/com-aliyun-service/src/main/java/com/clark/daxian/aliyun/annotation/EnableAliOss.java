package com.clark.daxian.aliyun.annotation;

import com.clark.daxian.aliyun.config.OssAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 阿里云Oss的启动注解
 * @author 大仙
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(OssAutoConfig.class)
public @interface EnableAliOss {
}
