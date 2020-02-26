package com.clark.daxian.api.aliyun;

import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;

/**
 * 阿里云视频点播业务
 * @author 大仙
 */
public interface VodService {
    /**
     * 获取播放权限
     * @param vid
     * @return
     */
    JSONObject getVideoPlayAuth(String vid);

    /**
     * 上传视频
     * @param source
     * @param cateId
     * @return
     */
    JSONObject uploadVideoToVOD(String source, Long cateId);

    /**
     * 通过文件上传
     * @param title
     * @param fileName
     * @param file
     * @return
     */
    String uploadVideoByStream(String title, String fileName,InputStream file);
}
