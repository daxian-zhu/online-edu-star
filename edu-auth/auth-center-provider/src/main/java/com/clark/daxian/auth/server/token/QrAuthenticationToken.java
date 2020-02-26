package com.clark.daxian.auth.server.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 二维码token
 * @Author: 朱维
 * @Date 16:32 2019/11/27
 */
public class QrAuthenticationToken extends MyAuthenticationToken {
    public QrAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public QrAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}