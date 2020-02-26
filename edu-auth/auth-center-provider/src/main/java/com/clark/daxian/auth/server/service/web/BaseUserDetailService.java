package com.clark.daxian.auth.server.service.web;

import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.auth.server.exception.AuthException;
import com.clark.daxian.auth.server.feign.UserService;
import com.clark.daxian.auth.server.token.BaseUserDetail;
import com.clark.daxian.auth.api.entity.BaseUser;
import com.clark.daxian.entity.user_center.Permission;
import com.clark.daxian.entity.user_center.Role;
import com.clark.daxian.entity.user_center.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 朱维
 * @Date 17:01 2019/11/27
 */
public abstract class BaseUserDetailService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 用户业务接口
     */
    @Autowired
    protected UserService userService;

    @Autowired
    private RedisTemplate<String, Permission> permissionRedisTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if(attributes==null){
            throw new AuthException("获取不到当先请求");
        }
        HttpServletRequest request = attributes.getRequest();
        String clientId = request.getParameter("client_id");
        User userInfo = getUser(username,clientId);

        List<GrantedAuthority> authorities = new ArrayList<>() ;
        //查询角色列表
        List<Role> roles = userService.listByUser(userInfo.getId()).getData();
        roles.forEach(role->{
            //只存储角色，所以不需要做区别判断
            authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
            List<Permission> permissions = userService.listByRole(role.getId()).getData();
            //存储权限到redis集合,保持颗粒度细化，当然也可以根据用户存储
            storePermission(permissions,role.getRoleCode());
        });
        // 返回带有用户权限信息的User
        org.springframework.security.core.userdetails.User user =
                new org.springframework.security.core.userdetails.User(
                        StringUtils.isBlank(userInfo.getTelephone())?userInfo.getEmail():userInfo.getTelephone(),
                        userInfo.getPassword(),
                        isActive(userInfo.getLoginStatus()),
                        true,
                        true,
                        true, authorities);
        BaseUser baseUser = new BaseUser();
        BeanUtils.copyProperties(userInfo,baseUser);
        return new BaseUserDetail(baseUser, user);
    }

    /**
     * 存储权限
     * @param permissions
     */
    private void storePermission(List<Permission> permissions,String roleCode){
        String redisKey = Constant.PERMISSIONS +roleCode;
        // 清除 Redis 中用户的角色
        permissionRedisTemplate.delete(redisKey);
        permissions.forEach(permission -> {
            permissionRedisTemplate.opsForList().rightPush(redisKey,permission);
        });
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
        if(1==active){
            return true;
        }
        return false;
    }

}