package com.clark.daxian.auth.server.filter;

import com.clark.daxian.api.wechat.WeChatComService;
import com.clark.daxian.auth.server.feign.WeChatService;
import com.clark.daxian.auth.server.token.OpenIdAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 二维码登陆：
 *       post: /qrLogin?qrCode=token
 * 生成二维码，建立 websocket - 后台
 * 用户在移动端扫码，回调事件到后台
 * 后台通过websocket通知前台登陆
 * 前台post:/qrLogin, qrCode为生成二维码时后台生成的key
 * @Author: 朱维
 * @Date 16:57 2019/11/27
 */
public class CodeLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WeChatService weChatService;

    private static final String SPRING_SECURITY_RESTFUL_OPENID_KEY = "code";

    private static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/code-login";
    private boolean postOnly = true;

    public CodeLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher(SPRING_SECURITY_RESTFUL_LOGIN_URL, "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        AbstractAuthenticationToken authRequest;
        String principal;
        String credentials;

        String code = obtainParameter(request, SPRING_SECURITY_RESTFUL_OPENID_KEY);
        if(!StringUtils.isNotBlank(code)){
            throw new UsernameNotFoundException("授权失败");
        }
        String openId = weChatService.getOpenId(code);
        redisTemplate.opsForValue().set(code,openId);
        // openId
        principal = openId;
        credentials = null;

        principal = principal.trim();
        authRequest = new OpenIdAuthenticationToken(principal, credentials);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void setDetails(HttpServletRequest request,
                            AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainParameter(HttpServletRequest request, String parameter) {
        String result =  request.getParameter(parameter);
        return result == null ? "" : result;
    }
}