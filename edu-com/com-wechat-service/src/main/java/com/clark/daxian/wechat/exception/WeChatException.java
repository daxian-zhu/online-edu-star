package com.clark.daxian.wechat.exception;

import com.clark.daxian.api.exception.EduException;

/**
 * WeChat业务异常
 * @author 大仙
 */
public class WeChatException extends EduException {


    public WeChatException(String message) {
        super(message);
    }

    public WeChatException(String message, int code) {
        super(message);
        this.code = code;
    }

    public WeChatException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }

}