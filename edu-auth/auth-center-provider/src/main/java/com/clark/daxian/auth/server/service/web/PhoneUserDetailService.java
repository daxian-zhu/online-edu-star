package com.clark.daxian.auth.server.service.web;

import com.clark.daxian.auth.server.exception.AuthException;
import com.clark.daxian.entity.user_center.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: 朱维
 * @Date 17:30 2019/11/27
 */
@Service
public class PhoneUserDetailService extends BaseUserDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected User getUser(String telephone, String clientId) {
        User user = userService.getUserByTel(telephone).getData();
        if(user==null){
            throw new AuthException("用户不存在");
        }
        return user;
    }
}
