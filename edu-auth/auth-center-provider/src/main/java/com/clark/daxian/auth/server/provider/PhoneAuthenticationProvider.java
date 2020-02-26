package com.clark.daxian.auth.server.provider;

import com.clark.daxian.aliyun.enums.SMSCodeType;
import com.clark.daxian.api.util.Md5Utils;
import com.clark.daxian.auth.server.token.PhoneAuthenticationToken;
import com.clark.daxian.auth.server.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 手机验证码登录
 * @Author: 朱维
 * @Date 16:26 2019/11/27
 */
public class PhoneAuthenticationProvider extends MyAbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {

        if(authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("PhoneAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            String telephone = authentication.getPrincipal().toString();
            this.logger.info("电话号码:"+telephone);
            // 验证码验证，调用公共服务查询 key 为authentication.getPrincipal()的value， 并判断其与验证码是否匹配
            String key =getSMSCode(telephone);
            if(key==null){
                this.logger.info("未获取redis存储的key:"+key);
                throw new UsernameNotFoundException("验证码不对");
            }
            Object smsCode = redisTemplate.opsForValue().get(key);
            if(smsCode==null){
                this.logger.info("redis中未找到验证码");
                throw new UsernameNotFoundException("验证码不对");
            }
            if(!(smsCode.toString()).equals(presentedPassword)){
                this.logger.debug("Authentication failed: verifyCode does not match stored value");
                throw new UsernameNotFoundException("验证码不对");
            }
        }
    }

    /**
     * 获取验证码
     * @return
     */
    private String getSMSCode(String telephone){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes!=null){
            HttpServletRequest request = attributes.getRequest();
            String ip = IPUtil.getIpAddress(request);
            this.logger.info("获取的IP："+ip);
            String key = Md5Utils.getMD5Uppercase(telephone+ SMSCodeType.LOGIN.name() +ip);
            return key;
        }
        return null;
    }
    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        PhoneAuthenticationToken result = new PhoneAuthenticationToken(principal, authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    protected UserDetails retrieveUser(String phone, Authentication authentication) throws AuthenticationException {
        UserDetails loadedUser;
        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(phone);
        } catch (UsernameNotFoundException var6) {
            throw var6;
        } catch (Exception var7) {
            throw new InternalAuthenticationServiceException(var7.getMessage(), var7);
        }

        if(loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        } else {
            return loadedUser;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PhoneAuthenticationToken.class.isAssignableFrom(authentication);
    }


    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}