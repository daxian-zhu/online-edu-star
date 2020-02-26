package com.clark.daxian.wechat.impl;

import com.clark.daxian.wechat.constant.WeChatConstant;
import com.clark.daxian.wechat.exception.WeChatException;
import com.clark.daxian.wechat.properties.WeChatProperties;
import com.clark.daxian.api.util.PubUtils;
import com.clark.daxian.wechat.util.WXPayConstants;
import com.clark.daxian.wechat.util.WXPayUtil;
import com.clark.daxian.api.wechat.WeChatPayService;
import com.clark.daxian.api.wechat.dto.WxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WeChatPayServiceImpl implements WeChatPayService {

    private Logger logger = LoggerFactory.getLogger(getClass());


    private WxPayService wxPayService;


    private WeChatProperties wxPayProperties;


    @Override
    public String payWeChatH5(WxPayRequest wxPayRequest) {
        logger.debug("微信H5支付，订单编号{}",wxPayRequest.getOrderNum());
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        //body消息体
        request.setBody(wxPayRequest.getBody());
        //订单编号
        request.setOutTradeNo(wxPayRequest.getOrderNum());
        //设备号
        request.setDeviceInfo("WEB");
        //货币类型
        request.setFeeType("CNY");
        //回调地址
        request.setNotifyUrl(wxPayRequest.getNotifyUrl());
        //调起方式-商户后台
        request.setTradeType("MWEB");
        //自定义参数
        request.setAttach(wxPayRequest.getAttach());
        //总金额及终端ip,支付金额以分为单位
        if (wxPayService.getConfig().isUseSandboxEnv()) {
            //沙箱环境
            request.setTotalFee(101);
            request.setSpbillCreateIp("123.12.12.123");
        } else {
            request.setTotalFee(changeBigDecimalToInteger(wxPayRequest.getPayMoney()));
            request.setSpbillCreateIp(wxPayRequest.getClientIp());
        }
        //商品ID
        request.setProductId(String.valueOf(wxPayRequest.getProductId()));
        //签名类型
        request.setSignType(WxPayConstants.SignType.MD5);
        WxPayUnifiedOrderResult wxPayUnifiedOrderResult = null;
        try {
            wxPayUnifiedOrderResult = wxPayService.unifiedOrder(request);
            if (wxPayUnifiedOrderResult == null) {
                throw new WeChatException("微信支付API返回信息为空");
            }
            if (!WeChatConstant.SUCCESS.equals(wxPayUnifiedOrderResult.getReturnCode())) {
                throw new WeChatException("微信支付API错误提示状态码:"+wxPayUnifiedOrderResult.getErrCode());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("微信H5支付失败,订单编号{},失败详情{}",wxPayRequest.getOrderNum(),e.getMessage());
            throw new WeChatException("下单失败",500);
        }
        if(wxPayUnifiedOrderResult==null||wxPayUnifiedOrderResult.getMwebUrl()==null){
            throw new WeChatException("下单失败",500);
        }
        return wxPayUnifiedOrderResult.getMwebUrl();
    }

    @Override
    public Map<String, String> createJSPay(WxPayRequest wxPayRequest) {
        logger.debug("微信公众号支付，订单编号{}",wxPayRequest.getOrderNum());
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        //body消息体
        request.setBody(wxPayRequest.getBody());
        //订单编号
        request.setOutTradeNo(wxPayRequest.getOrderNum());
        //设备号
        request.setDeviceInfo("WEB");
        //货币类型
        request.setFeeType("CNY");
        //回调地址
        request.setNotifyUrl(wxPayRequest.getNotifyUrl());
        //调起方式-商户后台
        request.setTradeType("JSAPI");
        //设置openId
        request.setOpenid(wxPayRequest.getOpenId());
        //自定义参数
        request.setAttach(wxPayRequest.getAttach());
        //总金额及终端ip,支付金额以分为单位
        if (wxPayService.getConfig().isUseSandboxEnv()) {
            //沙箱环境
            request.setTotalFee(101);
            request.setSpbillCreateIp("123.12.12.123");
        } else {
            request.setTotalFee(changeBigDecimalToInteger(wxPayRequest.getPayMoney()));
            request.setSpbillCreateIp(wxPayRequest.getClientIp());
        }
        //商品ID
        request.setProductId(String.valueOf(wxPayRequest.getProductId()));
        //签名类型
        request.setSignType(WxPayConstants.SignType.MD5);
        WxPayUnifiedOrderResult wxPayUnifiedOrderResult = null;
        try {
            wxPayUnifiedOrderResult = wxPayService.unifiedOrder(request);
            if (wxPayUnifiedOrderResult == null) {
                throw new WeChatException("微信支付API返回信息为空");
            }
            if (!WeChatConstant.SUCCESS.equals(wxPayUnifiedOrderResult.getReturnCode())) {
                throw new WeChatException("微信支付API错误提示状态码:"+wxPayUnifiedOrderResult.getErrCode());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("微信H5支付失败,订单编号{},失败详情{}",wxPayRequest.getOrderNum(),e.getMessage());
            throw new WeChatException("下单失败",500);
        }
        if(wxPayUnifiedOrderResult==null||wxPayUnifiedOrderResult.getPrepayId()==null){
            throw new WeChatException("下单失败",500);
        }
        String prepayId = wxPayUnifiedOrderResult.getPrepayId();
        //参数名称对应的参数值
        Map<String,String> valueMap = new HashMap<String,String>();
        //公众号
        valueMap.put("appId", wxPayProperties.getAppId());
        //时间戳
        valueMap.put("timeStamp", System.currentTimeMillis()/1000+"");
        //随机字符串
        valueMap.put("nonceStr", PubUtils.generateRandomNumbersCode(32).toUpperCase());
        //签名类型
        valueMap.put("signType", "MD5");
        //扩展字段
        valueMap.put("package", "prepay_id="+prepayId);
        //获取签名
        try {
            String paySign = WXPayUtil.generateSignature(valueMap, wxPayProperties.getMchKey(), WXPayConstants.SignType.MD5);
            valueMap.put("paySign", paySign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueMap;
    }


    /**
     * 转换bigDecimal到整型
     * @param bigDecimal
     * @return
     */
    private Integer changeBigDecimalToInteger(BigDecimal bigDecimal){
        return bigDecimal.multiply(new BigDecimal(100)).intValue();
    }

    public void setWxPayProperties(WeChatProperties wxPayProperties) {
        this.wxPayProperties = wxPayProperties;
    }

    public void setWxPayService(WxPayService wxPayService) {
        this.wxPayService = wxPayService;
    }
}
