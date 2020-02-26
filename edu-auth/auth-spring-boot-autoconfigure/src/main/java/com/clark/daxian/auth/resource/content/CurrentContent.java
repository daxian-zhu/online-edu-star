package com.clark.daxian.auth.resource.content;

import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.auth.api.entity.BaseUser;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public interface CurrentContent {

    /**
     * 获取用户token信息
     * @return
     */
    default Mono<JSONObject> getTokenInfo(){
        Mono<JSONObject> baseUser = ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(jwt->{
                    Jwt jwtValue = null;
                    if(jwt instanceof Jwt){
                        jwtValue = (Jwt)jwt;
                    }
                    JSONObject tokenInfo = JSONObject.parseObject(JSONObject.toJSONString(jwtValue.getClaims()));
                    return tokenInfo;
                });
        return baseUser;
    }

    /**
     * 获取用户信息
     * @return
     */
    default Mono<BaseUser> getUserInfo(){
        return getTokenInfo().map(token->token.getJSONObject(Constant.USER_INFO).toJavaObject(BaseUser.class));
    }
    /**
     * 获取当前请求
     * @return
     */
    default Mono<ServerHttpRequest> getRequest(){
        return ReactiveRequestContextHolder.getRequest();
    }
}
