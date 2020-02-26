package com.clark.daxian.entity.user_center;

import com.clark.daxian.entity.base.BaseEntity;
import com.clark.daxian.enums.user_center.PermissionType;
import com.clark.daxian.mybatis.annotation.Invisiable;
import com.clark.daxian.mybatis.annotation.Table;
import com.clark.daxian.mybatis.annotation.UserParent;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限表
 * @author 大仙
 */
@Data
@Table(name = "permission")
@UserParent
public class Permission extends BaseEntity implements Serializable {

    @Invisiable
    private static final long serialVersionUID = -7559506107525485531L;
    /**
     * 权限名称
     */
    private String authName;
    /**
     * 权限编码
     */
    private String authCode;
    /**
     * 权限类型
     */
    private PermissionType authType;
    /**
     * 路由地址
     */
    private String requestUrl;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 父权限
     */
    private Long parentPermission;
}
