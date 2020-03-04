package com.clark.daxian.im.service.impl;

import com.clark.daxian.auth.api.entity.BaseUser;
import com.clark.daxian.auth.api.entity.TokenEntity;
import com.clark.daxian.auth.api.util.TokenUtil;
import com.clark.daxian.im.feign.AuthService;
import com.clark.daxian.im.service.TokenCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * token检查实现类
 * @author 大仙
 */
@Service
public class TokenCheckServiceImpl implements TokenCheckService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, TokenEntity> redisTemplate;

    @Autowired
    private AuthService authService;

    @Override
    public Boolean checkTokenValid(String token) {
        BaseUser baseUser = TokenUtil.getBaseUserByToken(token);
        if(baseUser==null){
            return false;
        }
        if(!TokenUtil.judgeTokenValid(String.valueOf(baseUser.getId()),redisTemplate,token)){
            return false;
        }
        try {
            Map<Object, ?> result = authService.checkToken(token);
            logger.info("result:"+result);
            if(result.get("active") instanceof Boolean){
                Boolean active = (Boolean)result.get("active");
                if(active){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("token检查失败:"+e.getMessage());
        }
        return false;
    }
}
