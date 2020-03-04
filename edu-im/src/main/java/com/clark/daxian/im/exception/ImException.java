package com.clark.daxian.im.exception;

import com.clark.daxian.api.exception.EduException;

/**
 * IM业务相关异常
 * @author clark
 */
public class ImException extends EduException {


    public ImException(String message) {
        super(message);
    }

    public ImException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ImException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }
}