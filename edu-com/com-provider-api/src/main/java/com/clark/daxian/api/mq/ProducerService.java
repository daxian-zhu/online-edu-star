package com.clark.daxian.api.mq;

import com.clark.daxian.api.mq.entity.MqMessage;

/**
 * 发送消息
 * @author 大仙
 */
public interface ProducerService {

    /**
     * 发送消息
     * @param content
     * @param exchangeName
     * @param routingKey
     */
    void sendMsg(MqMessage content, String exchangeName, String routingKey);
}
