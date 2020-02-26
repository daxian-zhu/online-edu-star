package com.clark.daxian.aliyun.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.clark.daxian.aliyun.exception.AliyunException;
import com.clark.daxian.aliyun.properties.OssProperties;
import com.clark.daxian.api.aliyun.OssService;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * OSS业务实现
 * @author 大仙
 */
@Slf4j
public class OssServiceImpl implements OssService {


    private OssProperties ossProperties;

    @Override
    public String uploadByte(byte[] content, String key) {
        String bucket_resource = ossProperties.getBuckName();
        // 初始化OSSClient
        OSSClient client = new OSSClient(ossProperties.getPoint(), ossProperties.getKey(), ossProperties.getSecret());
        // 上传Object.
        try {
            client.putObject(bucket_resource, key, new ByteArrayInputStream(content));
        } catch (Exception e) {
            log.error("OSSUtils upload Exception", e);
            throw new AliyunException(e.getMessage());
        } finally {
            client.shutdown();
        }
        return ossProperties.getBaseUrl() + "/" + key;
    }

    @Override
    public String upload(String key, InputStream inputStream) {
        OSSClient ossClient = new OSSClient(ossProperties.getPoint(), ossProperties.getKey(), ossProperties.getSecret());
        try {
            ossClient.putObject(ossProperties.getBuckName(),  key, inputStream);
        } catch (Exception e) {
            log.error("OSSUtils upload Exception", e);
            throw new AliyunException(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return ossProperties.getBaseUrl() + "/" +  key;
    }



    @Override
    public JSONObject getToken(String key) {
        JSONObject result = new JSONObject();
        Map<String, String> tokenMessage = getSTSToken();
        if (tokenMessage.size() == 0) {
            log.info("没有查询到数据");
            return null;
        }
        result.put("Expiration", tokenMessage.get("Expiration"));
        result.put("AccessKey", tokenMessage.get("AccessKey"));
        result.put("AccessKeySecret", tokenMessage.get("AccessKeySecret"));
        result.put("SecurityToken", tokenMessage.get("SecurityToken"));
        result.put("endpoint", ossProperties.getPoint());
        result.put("bucket", ossProperties.getBuckName());
        result.put("key", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).toString() +key);
        return result;
    }


    /**
     * 获取TOKEN
     * @param source
     * @param userId
     * @return
     */
    /**
     * 获取STStoken
     *
     * @return
     */
    private Map<String, String> getSTSToken() {
        Map<String, String> tokenMessage = new HashMap<String, String>();
        String policy = null;
        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", ossProperties.getPoint());
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("cn-beijing", ossProperties.getKey(), ossProperties.getSecret());
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(ossProperties.getRoleArn());
            request.setRoleSessionName(ossProperties.getRoleSessionName());
            request.setPolicy(policy); // Optional
            request.setDurationSeconds(900L);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            tokenMessage.put("Expiration", response.getCredentials().getExpiration());
            tokenMessage.put("AccessKey", response.getCredentials().getAccessKeyId());
            tokenMessage.put("AccessKeySecret", response.getCredentials().getAccessKeySecret());
            tokenMessage.put("SecurityToken", response.getCredentials().getSecurityToken());
            tokenMessage.put("RequestId", response.getRequestId());
        } catch (ClientException e) {
            log.error("Error code: ", e.getErrCode());
            log.error("Error message: ", e.getErrMsg());
            log.error("RequestId: ", e.getRequestId());
            throw new AliyunException(e.getMessage());
        }
        return tokenMessage;
    }

    public void setOssProperties(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

}
