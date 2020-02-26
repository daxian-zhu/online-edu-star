package com.clark.daxian.mq.exception;

import com.clark.daxian.api.exception.EduException;

/**
 * 阿里云业务异常
 * @author 大仙
 */
public class MqException extends EduException {

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, int code) {
        super(message);
        this.code = code;
    }

    public MqException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }
}
