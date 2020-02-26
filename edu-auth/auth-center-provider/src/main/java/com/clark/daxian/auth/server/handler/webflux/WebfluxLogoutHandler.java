package com.clark.daxian.auth.server.handler.webflux;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.auth.api.entity.TokenEntity;
import com.clark.daxian.auth.api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 退出登录逻辑
 * @author 大仙
 */
public class WebfluxLogoutHandler implements ServerLogoutHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, TokenEntity> tokenEntityRedisTemplate;

    @Override
    public Mono<Void> logout(WebFilterExchange webFilterExchange, Authentication authentication) {
        logger.info("开始执行退出逻辑===");
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpRequest request = exchange.getRequest();

        // 获取Token
        String accessToken = request.getHeaders().getFirst(Constant.AUTHORIZATION);
        accessToken = accessToken.replace("Bearer ", "");
        String telephone = null;
        if (accessToken != null) {
            DecodedJWT jwt = JWT.decode(accessToken);
            telephone = String.valueOf(jwt.getClaims().get(Constant.USER_INFO).asMap().get("telephone"));
        }
        TokenUtil.logout(telephone,tokenEntityRedisTemplate,accessToken);
        logger.info("执行退出成功==");
        return null;
    }
}
