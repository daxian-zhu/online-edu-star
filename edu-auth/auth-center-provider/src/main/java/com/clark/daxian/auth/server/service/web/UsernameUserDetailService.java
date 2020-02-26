package com.clark.daxian.auth.server.service.web;

import com.clark.daxian.auth.server.exception.AuthException;
import com.clark.daxian.entity.user_center.User;
import org.springframework.stereotype.Service;

/**
 * @Author: 朱维
 * @Date 17:35 2019/11/27
 */
@Service
public class UsernameUserDetailService extends BaseUserDetailService {



    @Override
    protected User getUser(String email, String clientId) {
        User user = userService.getUserByEmail(email).getData();
        if(user==null){
            throw new AuthException("用户不存在");
        }
        return user;
    }
}
