package com.clark.daxian.api.response;

import com.clark.daxian.api.entity.Constant;
import lombok.Data;

/**
 * 通用响应
 * @param <T>
 */
@Data
public class ComResponse<T> {

    /**
     * 状态码
     */
    private int status;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 无返回数据成功响应
     * @return
     */
    public static ComResponse successResponse() {
        ComResponse response = new ComResponse();
        response.setStatus(Constant.SUCCESS_CODE);
        response.setMessage(Constant.SUCCESS_MSG);
        return response;
    }

    /**
     * 有返回数据成功响应
     * @param data 返回数据内容
     * @return
     */
    public static <T> ComResponse<T> successResponse(T data) {
        ComResponse<T> response = new ComResponse<>();
        response.setStatus(Constant.SUCCESS_CODE);
        response.setMessage(Constant.SUCCESS_MSG);
        response.setData(data);
        return response;
    }

    /**
     * 请求失败响应
     * @return
     */
    public static ComResponse failResponse() {
        ComResponse response = new ComResponse();
        response.setStatus(Constant.FAIL_CODE);
        response.setMessage(Constant.ERROR_MSG);
        return response;
    }

    /**
     * 请求失败
     * @param data
     * @param code
     * @param <T>
     * @return
     */
    public static <T> ComResponse<T> failResponse(T data,Integer code,String message){
        ComResponse<T> response = new ComResponse<>();
        response.setStatus(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

}
