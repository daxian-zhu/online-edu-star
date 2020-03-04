package com.clark.daxian.gateway.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.auth.api.entity.BaseUser;
import com.clark.daxian.auth.resource.content.CurrentContent;
import com.clark.daxian.auth.resource.util.PermissionUtil;
import com.clark.daxian.entity.user_center.Permission;
import com.clark.daxian.gateway.entity.UserResponse;
import com.clark.daxian.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户业务接口实现
 * @author 大仙
 */
@Service
public class UserServiceImpl implements UserService, CurrentContent {

    @Autowired
    private PermissionUtil permissionUtil;


    @Override
    public Mono<UserResponse> getUserInfoByAccess() {
        Mono<JSONObject> tokenInfo = getTokenInfo();
        return tokenInfo.map(token->{
            UserResponse userResponse  = new UserResponse();
            BaseUser baseUser = token.getJSONObject(Constant.USER_INFO).toJavaObject(BaseUser.class);
            userResponse.setBaseUser(baseUser);
            JSONArray array = token.getJSONArray("authorities");
            //查询全部的权限
            List<Permission> result = permissionUtil.getResultPermission(array);
            if(!CollectionUtils.isEmpty(result)) {
                userResponse.setAccess(result.stream().map(Permission::getAuthCode).collect(Collectors.toList()));
            }
            return userResponse;
        });
    }

}
