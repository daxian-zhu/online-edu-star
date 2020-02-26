package com.clark.daxian.entity;

import com.clark.daxian.api.mq.entity.MqMessage;
import lombok.Data;

@Data
public class User extends MqMessage {

    private String name;
}
