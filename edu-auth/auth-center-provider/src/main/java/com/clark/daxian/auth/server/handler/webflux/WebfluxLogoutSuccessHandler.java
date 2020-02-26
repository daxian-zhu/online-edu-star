package com.clark.daxian.auth.server.handler.webflux;

import com.clark.daxian.auth.server.util.RsponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * 退出成功处理逻辑
 * @author 大仙
 */
public class WebfluxLogoutSuccessHandler implements ServerLogoutSuccessHandler, RsponseUtil<String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return getResponse(webFilterExchange,objectMapper,"退出成功");
    }
}
