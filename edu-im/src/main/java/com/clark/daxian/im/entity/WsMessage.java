package com.clark.daxian.im.entity;

import com.clark.daxian.api.mq.entity.MqMessage;
import com.clark.daxian.im.enums.MessageType;
import lombok.Data;

/**
 * 聊天消息构造
 * @author 大仙
 */
@Data
public class WsMessage extends MqMessage {

    private Long from;

    private Long to;

    private String content;

    private MessageType messageType;

}
