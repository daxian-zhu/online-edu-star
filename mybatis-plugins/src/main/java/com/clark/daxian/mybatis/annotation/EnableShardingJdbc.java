package com.clark.daxian.mybatis.annotation;

import com.clark.daxian.mybatis.sharding.ShardingDataSourceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ShardingDataSourceConfig.class)
public @interface EnableShardingJdbc {

}
