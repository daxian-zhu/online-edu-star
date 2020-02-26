package com.clark.daxian.entity.user_center;

import com.clark.daxian.entity.base.BaseEntity;
import com.clark.daxian.mybatis.annotation.Invisiable;
import com.clark.daxian.mybatis.annotation.Table;
import com.clark.daxian.mybatis.annotation.UserParent;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色表
 * @author 大仙
 */
@Data
@UserParent
@Table(name = "role")
public class Role extends BaseEntity implements Serializable {

    @Invisiable
    private static final long serialVersionUID = 4791049986935562849L;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 说明
     */
    private String comment;
    /**
     * 状态
     */
    private Integer status;
}
