package com.clark.daxian.auth.resource.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * IP工具类
 * @author 大仙
 */
public class IPUtil {
    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     *
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     *
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(ServerHttpRequest request) {
        final String X_FORWARDED_HEADER = "X-Forwarded-For";
        final String X_REAL_HEADER = "X-Real-IP";
        final String UNKNOWN = "unknown";
        final String COMMA = ",";
        final String BLANK = "127.0.0.1";
        final List<String> forwardedHeaders = request.getHeaders().get(X_FORWARDED_HEADER);
        String ip = CollectionUtils.isEmpty(forwardedHeaders) ? BLANK : forwardedHeaders.get(0);
        if (StringUtils.isNotEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            int index = ip.indexOf(COMMA);
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        final List<String> realHeaders = request.getHeaders().get(X_REAL_HEADER);
        ip = CollectionUtils.isEmpty(realHeaders) ? BLANK : realHeaders.get(0);
        if (StringUtils.isNotEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddress() == null ? BLANK : request.getRemoteAddress().toString();
    }
}
