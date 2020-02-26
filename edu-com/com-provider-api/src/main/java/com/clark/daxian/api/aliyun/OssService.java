package com.clark.daxian.api.aliyun;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * 操作OSS的业务接口
 *
 * @author 大仙
 */
public interface OssService {
    /**
     * 通过字节数组上传
     *
     * @param content
     * @param key
     * @return
     * @throws IOException
     */
    String uploadByte(byte[] content, String key) throws IOException;

    /**
     * 通过流上传
     *
     * @param key
     * @param inputStream
     * @return
     */
    String upload(String key, InputStream inputStream);

    /**
     * 获取oss上传token
     * @return
     */
    JSONObject getToken(String key);
}
