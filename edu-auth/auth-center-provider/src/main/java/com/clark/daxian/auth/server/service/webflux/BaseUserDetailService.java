package com.clark.daxian.auth.server.service.webflux;

import com.clark.daxian.auth.server.token.BaseUserDetail;
import com.clark.daxian.auth.server.feign.UserService;
import com.clark.daxian.auth.api.entity.BaseUser;
import com.clark.daxian.entity.user_center.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 朱维
 * @Date 17:01 2019/11/27
 */
@Service
public abstract class BaseUserDetailService implements ReactiveUserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 用户业务接口
     */
    @Autowired
    protected UserService userService;


    @Override
    public Mono<UserDetails> findByUsername(String name){
        ServerHttpRequest request = ReactiveRequestContextHolder.getRequest().block();
        String clientId = request.getQueryParams().getFirst("client_Id");
        User userInfo = getUser(name,clientId);
        List<GrantedAuthority> authorities = new ArrayList<>() ;
        // 返回带有用户权限信息的User
        org.springframework.security.core.userdetails.User user =  new org.springframework.security.core.userdetails.User(name,
                userInfo.getPassword(), isActive(userInfo.getLoginStatus()), true, true, true, authorities);
        BaseUser baseUser = new BaseUser();
        BeanUtils.copyProperties(userInfo,baseUser);
        return Mono.just(new BaseUserDetail(baseUser, user));
    }

    /**
     * 获取用户
     * @param userName
     * @return
     */
    protected abstract User getUser(String userName,String clientId) ;

    /**
     * 是否有效的
     * @param active
     * @return
     */
    private boolean isActive(Integer active){
        if(active==1){
            return true;
        }
        return false;
    }
}