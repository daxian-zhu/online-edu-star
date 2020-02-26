package com.clark.daxian.user.service;

import com.clark.daxian.entity.user_center.Permission;

import java.util.List;

/**
 * 权限业务接口
 * @author 大仙
 */
public interface PermissionService {
    /**
     * 通过角色查询权限
     * @param roleId
     * @return
     */
    List<Permission> getPermissionByRole(Long roleId);
    /**
     * 保存权限
     * @param permission
     * @return
     */
    Long save(Permission permission);

    /**
     * 删除权限
     * @param id
     */
    void deletePermission(Long id);

    /**
     * 删除状态
     * @param id
     * @param status
     */
    void updateStatus(Long id,Integer status);

    /**
     * 根据系统查询全部权限
     * @return
     */
    List<Permission> all();
}
