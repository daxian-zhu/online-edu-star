package com.clark.daxian.auth.server.filter;

import com.clark.daxian.auth.server.token.PhoneAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 朱维
 * @Date 16:52 2019/11/27
 * /phoneLogin?telephone=13000000000&smsCode=1000
 */
public class PhoneLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 验证码登录请求参数：手机号码
     */
    private static final String SPRING_SECURITY_RESTFUL_PHONE_KEY = "telephone";
    /**
     * 验证码登录请求参数：短信验证码
     */
    private static final String SPRING_SECURITY_RESTFUL_VERIFY_CODE_KEY = "smsCode";
    /**
     * 验证码登录请求参数：登录地址
     */
    private static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/phone-login";
    private boolean postOnly = true;

    public PhoneLoginAuthenticationFilter() {
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

        // 手机验证码登陆
        principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_PHONE_KEY);
        credentials = obtainParameter(request, SPRING_SECURITY_RESTFUL_VERIFY_CODE_KEY);

        principal = principal.trim();
        authRequest = new PhoneAuthenticationToken(principal, credentials);

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