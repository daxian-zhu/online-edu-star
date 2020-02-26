package com.clark.daxian.api.mq.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author: 朱维
 * @Date 11:42 2019/12/10
 */
@Data
public class MqMessage implements Serializable {
    /**
     * 消息ID
     */
    protected String id = UUID.randomUUID().toString();
    /**
     * 单个消息过期时间
     */
    protected String expiration;
}
