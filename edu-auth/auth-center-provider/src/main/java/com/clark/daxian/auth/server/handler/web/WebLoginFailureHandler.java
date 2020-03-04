package com.clark.daxian.auth.server.handler.web;

import com.clark.daxian.auth.server.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: 朱维
 * @Date 1:55 2019/11/28
 */
public class WebLoginFailureHandler implements AuthenticationFailureHandler, ResponseUtil<String> {


    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg = null;
        if (exception instanceof BadCredentialsException) {
            msg = "账号或密码错误";
        } else {
            msg = exception.getMessage();
        }
        response.setStatus(500);
        getResponseWeb(response,objectMapper,msg);
    }
}
