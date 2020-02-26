package com.clark.daxian.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.wechat.constant.WeChatConstant;
import com.clark.daxian.wechat.exception.WeChatException;
import com.clark.daxian.wechat.properties.WeChatProperties;
import com.clark.daxian.wechat.util.HTTPUtil;
import com.clark.daxian.api.wechat.WeChatComService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class WeChatComServiceImpl implements WeChatComService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private WeChatProperties weChatProperties;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String getOpenId(String code) {
        String getOpenIdUrl = MessageFormat.format(WeChatConstant.GET_APPID_URL,weChatProperties.getAppId(),weChatProperties.getAppSecret(),code);
        logger.info("请求地址："+getOpenIdUrl);
        JSONObject result = HTTPUtil.publicGET(getOpenIdUrl);
        logger.info("获取OPENID:"+result.toString());
        if(result.get("openid")==null){
            throw new WeChatException("获取OPENID出错");
        }
        return result.get("openid").toString();
    }

    @Override
    public String getAuthUrl(String redirectUrl, String state) {
        if(StringUtils.isBlank(redirectUrl)){
            throw new WeChatException("缺失请求参数");
        }
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            throw new WeChatException("重定向地址编码失败",500);
        }
        logger.info("redirectUrl:"+redirectUrl);
        return MessageFormat.format(WeChatConstant.CODE_URL,weChatProperties.getAppId(),redirectUrl,state==null?"":state);
    }

    /**
     * 创建access_token
     * @return
     */
    private String createAccessToken(){
        String url =MessageFormat.format( WeChatConstant.TOKEN_URL,weChatProperties.getAppId(),weChatProperties.getAppSecret());
        JSONObject response = HTTPUtil.publicGET(url);
        logger.info("response:"+response.toJSONString());
        if(response!=null){
            if(response.get("access_token")!=null){
                return response.get("access_token").toString();
            }
        }
        throw new WeChatException(response.toJSONString());
    }

    @Override
    public String getAccessToken() {
        if(weChatProperties.getStartTokenCache()) {
            String token = redisTemplate.opsForValue().get(WeChatConstant.WECHAT_ACCESS_TOKEN);
            if (StringUtils.isBlank(token)) {
                token = createAccessToken();
                //保存一个小时，有效未2个小时
                redisTemplate.opsForValue().set(WeChatConstant.WECHAT_ACCESS_TOKEN, token, 3600, TimeUnit.SECONDS);
            }
            return token;
        }else{
            return createAccessToken();
        }
    }

    @Override
    public JSONObject getWeChaUserInfo(String openId) {
        String url =MessageFormat.format( WeChatConstant.GET_USERINFO,getAccessToken(),openId);
        JSONObject response =  HTTPUtil.publicGET(url);
        logger.info("微信用户信息："+JSONObject.toJSONString(response));
        return response;
    }

    public void setWeChatProperties(WeChatProperties weChatProperties) {
        this.weChatProperties = weChatProperties;
    }
}
