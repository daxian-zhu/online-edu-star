package com.clark.daxian.wechat.constant;

/**
 * @Author: 朱维
 * @Date 13:38 2019/11/29
 */
public class WeChatConstant {
    /**
     * 支付成功判断
     */
    public static final String SUCCESS = "SUCCESS";
    /**
     * 支付失败
     */
    public static final String FAIL = "FAIL";
    /**
     * 支付
     */
    public static final String SMS_TYPE = "PAY";
    /**
     * accesstoken的key
     */
    public static final String WECHAT_ACCESS_TOKEN= "wechat.access.token";
    /**
     * 获取code的url
     * //静默授权：https://open.weixin.qq.com/connect/oauth2/authorize?
     *              appid=wx520c15f417810387
     *              &redirect_uri=https%3A%2F%2Fchong.qq.com%2Fphp%2Findex.php%3Fd%3D%26c%3DwxAdapter%26m%3DmobileDeal%26showwxpaytitle%3D1%26vb2ctag%3D4_2030_5_1194_60
     *              &response_type=code
     *              &scope=snsapi_base
     *              &state=123#wechat_redirect
     * //非静默授权：https://open.weixin.qq.com/connect/oauth2/authorize?
     *              appid=wxf0e81c3bee622d60
     *              &redirect_uri=http%3A%2F%2Fnba.bluewebgame.com%2Foauth_response.php
     *              &response_type=code
     *              &scope=snsapi_userinfo
     *              &state=STATE#wechat_redirect
     */
    public static final String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state={2}#wechat_redirect";
    /**
     * 获取APPId的地址
     */
    public static final String GET_APPID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    /**
     * 获取token的URL
     */
    public static final String TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    /**
     * 获取用户信息
     */
    public static final String  GET_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang=zh_CN";
    /**
     * 发送模板消息
     */
    public static final String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";
    /**
     * 公众号二维码
     */
    public static final String WWCHATR_ER_CODE_URL = "https://m.wecode123.com/focuswx";

}
