package com.clark.daxian.im.util;

import com.clark.daxian.im.exception.ImException;
import org.apache.commons.lang3.StringUtils;

/**
 * 处理URL的工具类
 */
public class UrlUtil {

    /**
     * 获取请求参数
     * @param url
     * @param name
     * @return
     */
    public static String getParam(String url,String name){
        if(StringUtils.isBlank(url)||StringUtils.isBlank(name)){
            throw new  ImException("请确认查询参数");
        }
        String[] arr1 = url.split("\\?");
        if(arr1.length<2){
            throw new ImException("请携带请求凭证",403);
        }
        String[] params = arr1[1].split("&");
        for(String param :params){
            String[] map = param.split("=");
            if(map.length<2){
                continue;
            }
            if(StringUtils.isBlank(map[0])){
                continue;
            }
            if(map[0].equals(name)){
                return map[1];
            }
        }
        throw new  ImException("找不到该请求参数");
    }

    /**
     * 获取无参数的URL
     * @param url
     * @return
     */
    public static String getUrl(String url){
        if(StringUtils.isBlank(url)){
            throw new  ImException("请确认查询参数");
        }
        String[] arr1 = url.split("\\?");
        return arr1[0];
    }
}
