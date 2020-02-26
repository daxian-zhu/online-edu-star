package com.clark.daxian.config;

import com.clark.daxian.api.exception.EduException;
import com.clark.daxian.api.response.ComResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常捕获
 * @author 大仙
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理验证信息返回
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ComResponse handleBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return ComResponse.failResponse(null,500,fieldError.getDefaultMessage());
    }

    /**
     * 处理自定义异常
     * @param ex
     * @return
     */
    @ExceptionHandler(EduException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ComResponse handleOrderException(EduException ex) {
        return ComResponse.failResponse(null,ex.getCode()==null?500:ex.getCode(),ex.getMessage());
    }
}