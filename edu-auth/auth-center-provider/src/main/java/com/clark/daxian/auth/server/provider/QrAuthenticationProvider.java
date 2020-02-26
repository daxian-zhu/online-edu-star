package com.clark.daxian.auth.server.provider;

import com.clark.daxian.auth.server.token.QrAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @Author: 朱维
 * @Date 16:28 2019/11/27
 */
public class QrAuthenticationProvider extends MyAbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {

        // 二维码只需要根据 SPRING_SECURITY_RESTFUL_QR_CODE_KEY 查询到用户即可，所以此处无需验证
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        QrAuthenticationToken result = new QrAuthenticationToken(principal, authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    protected UserDetails retrieveUser(String qrCode, Authentication authentication) throws AuthenticationException {
        UserDetails loadedUser;
        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(qrCode);
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
        return QrAuthenticationToken.class.isAssignableFrom(authentication);
    }


    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
