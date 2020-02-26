package com.clark.daxian.aliyun.exception;

import com.clark.daxian.api.exception.EduException;

/**
 * 阿里云业务异常
 * @author 大仙
 */
public class AliyunException extends EduException {

    public AliyunException(String message) {
        super(message);
    }

    public AliyunException(String message, int code) {
        super(message);
        this.code = code;
    }

    public AliyunException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }
}
