package com.clark.daxian.auth.api.util;

import com.clark.daxian.auth.api.entity.TokenEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * token控制工具类
 * @author 大仙
 */
public class TokenUtil implements Serializable {

    private static final long serialVersionUID = 8617969696670516L;

    /**
     * 存储token
     * @param id
     * @param redisTemplate
     * @param token
     * @return
     */
    public static Boolean pushToken(String id, RedisTemplate<String, TokenEntity> redisTemplate, String token, Date invalid,Integer max){
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
     * @param id
     * @param redisTemplate
     * @param token
     * @return true 有效 false: 无效
     */
    public static Boolean judgeTokenValid(String id, RedisTemplate<String, TokenEntity> redisTemplate, String token){
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
     * @param id
     * @param redisTemplate
     * @param token
     */
    public static void logout(String id, RedisTemplate<String, TokenEntity> redisTemplate, String token){
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
