package com.clark.daxian.api.wechat.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 请求支付相关参数
 */
@Data
@Builder
public class WxPayRequest {
    /**
     * 商品信息
     */
    private String body;
    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 公众号Id
     */
    private String openId;
    /**
     * 实际支付金额
     */
    private BigDecimal payMoney;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * ip
     */
    private String clientIp;
    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 自定义参数（json对象）
     */
    private String attach;

}
