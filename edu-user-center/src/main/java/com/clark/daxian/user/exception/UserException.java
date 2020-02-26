package com.clark.daxian.user.exception;

import com.clark.daxian.api.exception.EduException;

/**
 * 自定义用户业务异常
 * @author 大仙
 */
public class UserException extends EduException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, int code) {
        super(message);
        this.code = code;
    }

    public UserException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }
}