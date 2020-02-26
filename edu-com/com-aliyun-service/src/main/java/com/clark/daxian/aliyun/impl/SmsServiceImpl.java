package com.clark.daxian.aliyun.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.clark.daxian.aliyun.exception.AliyunException;
import com.clark.daxian.aliyun.properties.SmsProperties;
import com.clark.daxian.api.aliyun.SmsService;
import com.clark.daxian.api.enums.SMSTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.LimitExceededException;

/**
 * 短信实现
 * @author 大仙
 */
@Slf4j
public class SmsServiceImpl implements SmsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private SmsProperties smsProperties;


    @Override
    public void sendSMS(String phoneNum, SMSTemplate templateCode, String params) {
        send(phoneNum, templateCode.toString(), params);
    }
    /**
     * 发送短信
     * @param phoneNum    接收短信内容
     * @param templateCode
     * @param param
     * @return
     * @throws ClientException
     * @throws LimitExceededException
     */
    private boolean send(String phoneNum, String templateCode, String param) {
        try {
            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile(smsProperties.getEndpoint(), smsProperties.getKey(),
                    smsProperties.getSecret());
            DefaultProfile.addEndpoint(smsProperties.getEndpoint(), smsProperties.getEndpoint(), smsProperties.getProduct(), smsProperties.getDomain());
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            request.setConnectTimeout(3000);
            request.setReadTimeout(10000);
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
            request.setPhoneNumbers(phoneNum);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(smsProperties.getSignName());
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam(param);
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            logger.info("发送短信结果：" + JSONObject.toJSONString(sendSmsResponse));
            if (sendSmsResponse.getCode() != null) {
                if (sendSmsResponse.getCode().equals("OK")) {
                    return true;
                }
                if (sendSmsResponse.getCode().equals("isv.BUSINESS_LIMIT_CONTROL")) {
                    throw new AliyunException("短信服务限流");
                }
            }
        }catch (Exception e){
            throw new AliyunException(e.getMessage());
        }
        return false;

    }

    public void setSmsProperties(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }
}
