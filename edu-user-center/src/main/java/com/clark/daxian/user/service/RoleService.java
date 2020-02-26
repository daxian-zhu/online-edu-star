package com.clark.daxian.user.service;

import com.clark.daxian.entity.user_center.Role;

import java.util.List;

/**
 * 角色业务接口
 * @author 大仙
 */
public interface RoleService {

    /**
     * 根据用户查询角色列表
     * @param userId
     * @return
     */
    List<Role> getRoleByUser(Long userId);

    /**
     * 保存角色
     * @param role
     * @return
     */
    Long save(Role role);

    /**
     * 删除角色
     * @param id
     */
    void deleteRole(Long id);

    /**
     * 修改状态
     * @param id
     * @param status
     */
    void updateStatus(Long id,Integer status);
}
