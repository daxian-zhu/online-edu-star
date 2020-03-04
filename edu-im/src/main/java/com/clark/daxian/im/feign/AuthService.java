package com.clark.daxian.im.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 调用检查token的方法
 * @author clark
 */
@FeignClient(name = "oauth2-server")
public interface AuthService {

    /**
     * 检查token
     * @param token
     * @return
     */
    @GetMapping("/oauth/check_token")
    Map checkToken(@RequestParam("token") String token);
}
