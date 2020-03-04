package com.clark.daxian.im.service;

/**
 * 检查token的有效性
 * @author 朱维
 */
public interface TokenCheckService {
    /**
     * 检查token是否有效
     * @param token
     * @return
     */
    Boolean checkTokenValid(String token);
}
