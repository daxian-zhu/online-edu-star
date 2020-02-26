package com.clark.daxian.auth.server.exception;

import com.clark.daxian.api.exception.EduException;

/**
 * 权限相关异常
 * @author 大仙
 */
public class AuthException extends EduException {


    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, int code) {
        super(message);
        this.code = code;
    }

    public AuthException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }
}
