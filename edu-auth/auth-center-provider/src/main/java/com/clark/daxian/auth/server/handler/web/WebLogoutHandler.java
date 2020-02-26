package com.clark.daxian.auth.server.handler.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.auth.api.entity.TokenEntity;
import com.clark.daxian.auth.api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录逻辑
 * @author 大仙
 */
public class WebLogoutHandler implements LogoutHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, TokenEntity> tokenEntityRedisTemplate;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logger.info("开始执行退出逻辑===");
        // 获取Token
        String accessToken = request.getHeader(Constant.AUTHORIZATION);
        accessToken = accessToken.replace("Bearer ", "");
        String id = null;
        if (accessToken != null) {
            DecodedJWT jwt = JWT.decode(accessToken);
            id = String.valueOf(jwt.getClaims().get(Constant.USER_INFO).asMap().get("id"));
        }
        TokenUtil.logout(id,tokenEntityRedisTemplate,accessToken);
        logger.info("执行退出成功==");
    }
}
