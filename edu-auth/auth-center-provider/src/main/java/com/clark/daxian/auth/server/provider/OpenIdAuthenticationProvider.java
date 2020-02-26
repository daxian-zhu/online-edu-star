package com.clark.daxian.auth.server.provider;

import com.clark.daxian.auth.server.token.OpenIdAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 手机验证码登录
 * @Author: 朱维
 * @Date 16:26 2019/11/27
 */
public class OpenIdAuthenticationProvider extends MyAbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {
        //只需要根据openId能查询到用户即可，无需验证
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        OpenIdAuthenticationToken result = new OpenIdAuthenticationToken(principal, authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    protected UserDetails retrieveUser(String openId, Authentication authentication) throws AuthenticationException {
        UserDetails loadedUser;
        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(openId);
        } catch (UsernameNotFoundException var6) {
            throw var6;
        } catch (Exception var7) {
            throw new InternalAuthenticationServiceException(var7.getMessage(), var7);
        }

        if(loadedUser == null) {
            throw new UsernameNotFoundException("该OPENID未绑定学员账号");
        } else {
            return loadedUser;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
    }


    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}