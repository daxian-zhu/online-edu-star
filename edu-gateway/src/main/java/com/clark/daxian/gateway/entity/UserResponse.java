package com.clark.daxian.gateway.entity;

import com.clark.daxian.auth.api.entity.BaseUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息节课
 */
@Data
public class UserResponse implements Serializable {


    private static final long serialVersionUID = 5291438641174821152L;
    /**
     * 用户信息
     */
    private BaseUser baseUser;
    /**
     * 权限列表
     */
    private List<String> access;
}
