package com.clark.daxian.api.aliyun;

import com.clark.daxian.api.enums.SMSTemplate;

/**
 * 短信业务接口
 * @author 大仙
 */
public interface SmsService {
    /**
     * 发送短信
     * @param phoneNum
     * @param templateCode
     * @param params
     */
    void sendSMS(String phoneNum, SMSTemplate templateCode, String params);
}
