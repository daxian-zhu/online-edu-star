package com.clark.daxian.auth.server.handler.webflux;

import com.clark.daxian.auth.server.util.RsponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

/**
 * @Author: 朱维
 * @Date 1:55 2019/11/28
 */
public class WebfluxLoginFailureHandler implements ServerAuthenticationFailureHandler, RsponseUtil<String> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        String msg = null;
        if (exception instanceof BadCredentialsException) {
            msg = "账号或密码错误";
        } else {
            msg = "认证失败:"+exception.getMessage();
        }
        return getResponse(webFilterExchange,objectMapper,msg);
    }
}
