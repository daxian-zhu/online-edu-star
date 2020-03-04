package com.clark.daxian.auth.api.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.api.exception.EduException;
import com.clark.daxian.auth.api.entity.BaseUser;
import com.clark.daxian.auth.api.entity.TokenEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * token控制工具类
 * @author 大仙
 */
@Slf4j
public class TokenUtil implements Serializable {

    private static final long serialVersionUID = 8617969696670516L;

    public static final String KEYPER = "token:";

    /**
     * 获取用户
     * @return
     */
    public static BaseUser getUser(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            throw new EduException("获取不到当前请求");
        }
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(Constant.AUTHORIZATION);
        return getBaseUserByToken(token);
    }
    /**
     * 根据token获取用户
     * @param token
     * @return
     */
    public static BaseUser getBaseUserByToken(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String,Object> map = jwt.getClaim(Constant.USER_INFO).asMap();
            return JSONObject.parseObject(JSONObject.toJSONString(map)).toJavaObject(BaseUser.class);
        }catch (Exception e){
            log.error("解析token失败:"+e.getMessage());
            return null;
        }
    }


    /**
     * 存储token
     * @param key
     * @param redisTemplate
     * @param token
     * @return
     */
    public static Boolean pushToken(String key, RedisTemplate<String, TokenEntity> redisTemplate, String token, Date invalid,Integer max){
        String id = KEYPER+key;
        LocalDateTime invalidDate = invalid.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long size = redisTemplate.opsForList().size(id);
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setInvalidDate(invalidDate);
        tokenEntity.setToken(token);
        if(size<=0){
            redisTemplate.opsForList().rightPush(id,tokenEntity);
        }else{
            List<TokenEntity> tokenEntities = redisTemplate.opsForList().range(id, 0, size);
            tokenEntities = tokenEntities.stream().filter(te -> te.getInvalidDate().isAfter(LocalDateTime.now())).collect(Collectors.toList());
            if(tokenEntities.size()>= max){
                return false;
            }
            tokenEntities.add(tokenEntity);
            redisTemplate.delete(id);
            tokenEntities.forEach(te->{
                redisTemplate.opsForList().rightPush(id,te);
            });
        }
        return true;
    }

    /**
     * 判断token是否有效
     * @param key
     * @param redisTemplate
     * @param token
     * @return true 有效 false: 无效
     */
    public static Boolean judgeTokenValid(String key, RedisTemplate<String, TokenEntity> redisTemplate, String token){
        String id = KEYPER+key;
        long size = redisTemplate.opsForList().size(id);
        if(size<=0){
            return false;
        }else{
            List<TokenEntity> tokenEntities = redisTemplate.opsForList().range(id, 0, size);
            tokenEntities = tokenEntities.stream().filter(te->te.getToken().equals(token)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(tokenEntities)){
                return false;
            }
            TokenEntity tokenEntity = tokenEntities.get(0);
            if(tokenEntity.getInvalidDate().isAfter(LocalDateTime.now())&&tokenEntity.getStatus()==1){
                return true;
            }
        }
        return false;
    }

    /**
     * 登出
     * @param key
     * @param redisTemplate
     * @param token
     */
    public static void logout(String key, RedisTemplate<String, TokenEntity> redisTemplate, String token){
        String id = KEYPER+key;
        long size = redisTemplate.opsForList().size(id);
        if(size<=0){
            redisTemplate.delete(id);
        }else{
            List<TokenEntity> tokenEntities = redisTemplate.opsForList().range(id, 0, size);
            tokenEntities = tokenEntities.stream().filter(te->!te.getToken().equals(token)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(tokenEntities)){
                redisTemplate.delete(id);
            }
            redisTemplate.delete(id);
            tokenEntities.forEach(te->{
                redisTemplate.opsForList().rightPush(id,te);
            });
        }
    }
}
