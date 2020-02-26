package com.clark.daxian.api.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密工具
 * @author 大仙
 *
 */
public class Md5Utils {
	 /**
     * MD5数字签名
     * @param str
     * @return
     * @throws Exception
     */
	public static byte[] getMD5(String str){
		 // 定义数字签名方法, 可用：MD5, SHA-1
		byte[] b = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try {
                b = md.digest(str.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return b;
	}
	/**
	 * MD5加密
	 * @param str
	 * @return 大写32位字符串
	 */
    public static String getMD5Uppercase(String str) {
    	byte[] b = getMD5(str);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1)
                sb.append("0");
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }
    
    /**
	 * MD5加密
	 * @param str
	 * @return 小写32位字符串
	 */
    public static String getMD5Lowercase(String str) {
    	byte[] b = getMD5(str);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1)
                sb.append("0");
            sb.append(s);
        }
        return sb.toString();
    }
    
}
