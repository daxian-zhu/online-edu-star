package com.clark.daxian.auth.server.service.web;

import com.clark.daxian.entity.user_center.User;
import org.springframework.stereotype.Service;

/**
 * @Author: 朱维
 * @Date 17:33 2019/11/27
 */
@Service
public class QrUserDetailService extends BaseUserDetailService {

    @Override
    protected User getUser(String userName, String clientId) {
        return null;
    }
}
