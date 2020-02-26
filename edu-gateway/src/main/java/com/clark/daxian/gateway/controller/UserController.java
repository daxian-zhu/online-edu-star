package com.clark.daxian.gateway.controller;

import com.clark.daxian.gateway.entity.UserResponse;
import com.clark.daxian.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 用户控制器
 * @author 大仙
 */
@RestController
public class UserController  {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/user")
    public Mono<UserResponse> getUserInfo(){
        return userService.getUserInfoByAccess();
    }
}
