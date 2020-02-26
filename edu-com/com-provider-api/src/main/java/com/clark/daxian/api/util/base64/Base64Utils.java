package com.clark.daxian.api.util.base64;

import java.io.IOException;

/**
 * base64工具类
 * @author 大仙
 */
public class Base64Utils {
    /**
     * 编码
     * @param bStr
     * @return String
     */
    public static String encode(byte[] bStr){
        return new sun.misc.BASE64Encoder().encode(bStr);
    }

    /**
     * 解码
     * @param str
     * @return string
     */
    public static String decode(String str){
        try {
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            byte[] bt = decoder.decodeBuffer( str );
            return new String(bt,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}