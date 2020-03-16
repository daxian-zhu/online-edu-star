package com.clark.daxian.entity.user_center;


import com.clark.daxian.entity.base.BaseEntity;
import com.clark.daxian.mybatis.annotation.Invisiable;
import com.clark.daxian.mybatis.annotation.Table;
import com.clark.daxian.mybatis.annotation.UserParent;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体
 */
@Data
@Table(name = "user")
@UserParent
public class User extends BaseEntity implements Serializable {

    @Invisiable
    private static final long serialVersionUID = 5662750052896147675L;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 邮箱，用户企业人员进行登录
     */
    private String email;
    /**
     * 电话号码，用户客户登录
     */
    private String telephone;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String headerUrl;
    /**
     * 登录状态
     * 1：允许 0：不允许
     */
    private Integer loginStatus = 1;
}
