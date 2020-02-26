package com.clark.daxian.api.wechat;

import com.clark.daxian.api.wechat.dto.WxPayRequest;

import java.util.Map;

/**
 * 微信支付相关
 */
public interface WeChatPayService {

    /**
     * 微信H5支付
     * @param wxPayRequest 支付必要参数
     * @return CommonResponse
     */
    String payWeChatH5(WxPayRequest wxPayRequest);

    /**
     * 构建JS支付
     * @param wxPayRequest
     * @return
     */
    Map<String,String> createJSPay(WxPayRequest wxPayRequest);
}
