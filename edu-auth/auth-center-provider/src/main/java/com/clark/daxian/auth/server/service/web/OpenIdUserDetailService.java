package com.clark.daxian.auth.server.service.web;

import com.clark.daxian.entity.user_center.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: 朱维
 * @Date 2:57 2019/11/28
 */
@Service
public class OpenIdUserDetailService extends BaseUserDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected User getUser(String openId, String clientId) {

        return null;
    }
}