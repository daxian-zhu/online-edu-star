package com.clark.daxian.api.wechat;

import com.alibaba.fastjson.JSONObject;

/**
 * WeChat共业务模块
 */
public interface WeChatComService {
    /**
     * 获取openId
     * @param code
     * @return
     */
    String getOpenId(String code);
    /**
     * 获取授权地址
     * @param redirectUrl
     * @param state 透传参数
     * @return
     */
    String getAuthUrl(String redirectUrl, String state);

    /**
     * 获取Token
     * @return
     */
    String getAccessToken();

    /**
     * 获取用户信息
     * @param openId
     * @return
     */
    JSONObject getWeChaUserInfo(String openId);
}
