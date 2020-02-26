package com.clark.daxian.auth.resource.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.auth.api.entity.BaseUser;
import com.clark.daxian.auth.api.entity.TokenEntity;
import com.clark.daxian.auth.api.util.TokenUtil;
import com.clark.daxian.auth.resource.util.PermissionUtil;
import com.clark.daxian.entity.user_center.Permission;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义授权管理器
 */
@Slf4j
@ConfigurationProperties(prefix = "edu.security")
public class WebfluxReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private String[] ignoreds;

    private String[] notRoles;

    @Autowired
    private RedisTemplate<String, TokenEntity> redisTemplate;

    @Autowired
    private RedisTemplate<String, Permission> permissionRedisTemplate;

    @Autowired
    private PermissionUtil permissionUtil;

    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        //获取请求
        ServerHttpRequest request =  authorizationContext.getExchange().getRequest();
        //判断当前是否有接口权限
        String url =request.getPath().value();
        log.debug("请求url:{}",url);
        String httpMethod = request.getMethod().name();
        log.debug("请求方法:{}",httpMethod);
        //如果是OPTIONS的请求直接放过
        if(HttpMethod.OPTIONS.name().equals(httpMethod)){
            return Mono.just(new AuthorizationDecision(true));
        }
        log.debug("白名单："+ Arrays.toString(ignoreds));
        // 不拦截的请求
        for (String path : ignoreds) {
            String temp = path.trim();
            if (matcher.match(temp, url)) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        log.debug("不需要角色权限判断的接口：{}",Arrays.toString(notRoles));
        for (String path : notRoles) {
            String temp = path.trim();
            if (matcher.match(temp, url)) {
                //对于不需要验证角色的接口，只要token验证成功返回成功即可
                return authentication.map(a ->  {
                    if(a.isAuthenticated()){
                        return new AuthorizationDecision(true);
                    }else{
                        return new AuthorizationDecision(false);
                    }
                }).defaultIfEmpty(new AuthorizationDecision(false));
            }
        }
        //需要进行权限验证的
        return
                //过滤验证成功的
                authentication.filter(a ->  a.isAuthenticated())
                        //转换成Flux
                    .flatMapIterable(a -> {
                        Jwt jwtValue = null;
                        if(a.getPrincipal() instanceof Jwt){
                            jwtValue = (Jwt)a.getPrincipal();
                        }
                        JSONObject tokenInfo = JSONObject.parseObject(JSONObject.toJSONString(jwtValue.getClaims()));
                        BaseUser baseUser = tokenInfo.getJSONObject(Constant.USER_INFO).toJavaObject(BaseUser.class);
                        //存储当前数据
                        List<AuthUser> authUsers = new ArrayList<>();
                        JSONArray array = tokenInfo.getJSONArray("authorities");
                        for (int i = 0;i<array.size();i++){
                            AuthUser authUser = new AuthUser();
                            authUser.setBaseUser(baseUser);
                            authUser.setAuthority(array.get(i).toString());
                            authUsers.add(authUser);
                        }
                        return authUsers;
                    })
                     //转成成权限名称
                    .any(c-> {//检测权限是否匹配
                        //获取当前用户
                        BaseUser baseUser = c.getBaseUser();
                        //判断当前携带的Token是否有效
                        String  token = request.getHeaders().getFirst(Constant.AUTHORIZATION).replace("Bearer ","");
                        if(!TokenUtil.judgeTokenValid(String.valueOf(baseUser.getId()),redisTemplate,token)){
                            return false;
                        }
                        //获取当前权限
                        String authority = c.getAuthority();
                        //通过当前权限码查询可以请求的地址
                        log.debug("当前权限是：{}",authority);
                        List<Permission> permissions = permissionUtil.getResultPermission(authority);
                        permissions = permissions.stream().filter(permission -> StringUtils.isNotBlank(permission.getRequestUrl())).collect(Collectors.toList());
                        //请求URl匹配，放行
                        if(permissions.stream().anyMatch(permission -> matcher.match(permission.getRequestUrl(),url))){
                            return true;
                        }
                        return false;
                    })
                    .map(hasAuthority ->  new AuthorizationDecision(hasAuthority)).defaultIfEmpty(new AuthorizationDecision(false));
    }


    /**
     * 获取当前用户的权限集合
     * @param authority
     * @return
     */
    private List<Permission> getPermissions(String authority){
        String redisKey = Constant.PERMISSIONS+authority;
        long size = permissionRedisTemplate.opsForList().size(redisKey);
        List<Permission> permissions = permissionRedisTemplate.opsForList().range(redisKey, 0, size);
        return permissions;
    }

    public void setIgnored(String ignored) {
        ignored = org.springframework.util.StringUtils.trimAllWhitespace(ignored);
        if (ignored != null && !"".equals(ignored)) {
            this.ignoreds = ignored.split(",");
        } else {
            this.ignoreds = new String[]{};
        }
    }

    public void setNotRole(String notRole) {
        notRole = org.springframework.util.StringUtils.trimAllWhitespace(notRole);
        if (notRole != null && !"".equals(notRole)) {
            this.notRoles = notRole.split(",");
        } else {
            this.notRoles = new String[]{};
        }
    }

    /**
     * 构造对象
     */
    @Data
    class AuthUser{
        private String authority;

        private BaseUser baseUser;
    }
}
