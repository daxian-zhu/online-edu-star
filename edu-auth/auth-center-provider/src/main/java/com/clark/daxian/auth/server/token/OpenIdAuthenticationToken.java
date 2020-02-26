package com.clark.daxian.auth.server.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * OPENDID的token
 * @Author: 朱维
 * @Date 16:32 2019/11/27
 */
public class OpenIdAuthenticationToken extends MyAuthenticationToken {
    public OpenIdAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public OpenIdAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}