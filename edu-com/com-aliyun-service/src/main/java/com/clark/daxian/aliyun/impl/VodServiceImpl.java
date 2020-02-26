package com.clark.daxian.aliyun.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.clark.daxian.aliyun.properties.VodProperties;
import com.clark.daxian.api.aliyun.VodService;
import com.clark.daxian.api.util.PubUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 视频点播业务
 * @author 大仙
 */
@Slf4j
public class VodServiceImpl implements VodService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 正常
     */
    private static final String STATUS = "Normal";

    private VodProperties vodProperties;

    public void setVodProperties(VodProperties vodProperties) {
        this.vodProperties = vodProperties;
    }

    @Override
    public JSONObject getVideoPlayAuth(String vid) {
        DefaultProfile profile = DefaultProfile.getProfile(vodProperties.getRegion(), vodProperties.getKey(), vodProperties.getSecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(vid);
        request.setAuthInfoTimeout(3000L);
        GetVideoPlayAuthResponse response = null;
        try {
            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            throw new RuntimeException("GetVideoPlayAuthRequest Server failed");
        } catch (ClientException e) {
            throw new RuntimeException("GetVideoPlayAuthRequest Client failed");
        }
        JSONObject result = new JSONObject();
        result.put("status", false);
        try {
            if (STATUS.equals(response.getVideoMeta().getStatus())) {
                result.put("status", true);
            }
            result.put("playAuth", response.getPlayAuth()); // 播放凭证
        } catch (Exception e) {
            logger.error("获取播放凭证出错");
        }
        return result;
    }

    @Override
    public JSONObject uploadVideoToVOD(String source, Long cateId) {
        DefaultProfile profile = DefaultProfile.getProfile(vodProperties.getRegion(), vodProperties.getKey(), vodProperties.getSecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        String randomCode = System.currentTimeMillis() + PubUtils.generateRandomFullCode(5);
        String fileName = source + "_" + randomCode + ".mp4";
        //封面图片
        String coverUrl = vodProperties.getCoverUrl() + source + "_" + randomCode + ".jpg";
        String title = fileName;
        request.setFileName(fileName);
        request.setTitle(title);
        request.setCateId(cateId);
        request.setCoverURL(coverUrl);
        request.setTemplateGroupId(vodProperties.getTemplateMode());
        CreateUploadVideoResponse response = null;
        try {
            response = client.getAcsResponse(request);
        } catch (ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        logger.info("result:{}",JSONObject.toJSONString(response));
        /*System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        System.out.print("VideoId=" + response.getVideoId() + "\n");
        System.out.print("uploadAddress=" + response.getUploadAddress() + "\n");
        System.out.print("uploadAuth=" + response.getUploadAuth() + "\n");*/
        JSONObject result = new JSONObject();
        result.put("videoId", response.getVideoId());
        result.put("uploadAddress", response.getUploadAddress());
        result.put("uploadAuth", response.getUploadAuth());
        result.put("fileName", fileName);
        result.put("region", vodProperties.getRegion());
        result.put("coverUrl", coverUrl);
        result.put("imageFileName", source + "_" + randomCode + ".jpg");
        result.put("uid", vodProperties.getUid());
        return result;
    }

    /**
     * 上传视频
     * @param title
     * @param fileName
     * @param file
     * @return
     */
    @Override
    public String uploadVideoByStream(String title, String fileName, InputStream file) {
        UploadStreamRequest request =  new UploadStreamRequest(vodProperties.getKey(), vodProperties.getSecret(), title, fileName, file);
        request.setCateId(1000066766L);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n"); //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
        return response.getVideoId();
    }
}
