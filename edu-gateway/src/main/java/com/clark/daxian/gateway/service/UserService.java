package com.clark.daxian.gateway.service;

import com.clark.daxian.gateway.entity.UserResponse;
import reactor.core.publisher.Mono;

/**
 * 用户相关业务接口
 * @author 大仙
 */
public interface UserService {
    /**
     * 获取用户信息
     * @return
     */
    Mono<UserResponse> getUserInfoByAccess();
}
