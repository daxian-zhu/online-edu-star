package com.clark.daxian.mybatis.sharding;

import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.core.api.config.MasterSlaveRuleConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>  </b>
 * <p>
 * 功能描述:配置DataSource
 * </p>
 * <p/>
 * @author 朱维
 * @date 2018年10月17日
 * @time 上午9:40:20
 * @Path: com.xuebaclass.crm.conf.ShardingMasterSlaveConfig.java
 */
@Data
@ConfigurationProperties(prefix = "sharding.jdbc")
public class ShardingMasterSlaveConfig {

    private Map<String, DruidDataSource> dataSources = new HashMap<>();

    private MasterSlaveRuleConfiguration masterSlaveRule;
}
