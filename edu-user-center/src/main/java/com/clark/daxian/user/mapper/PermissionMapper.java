package com.clark.daxian.user.mapper;

import com.clark.daxian.entity.user_center.Permission;
import com.clark.daxian.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 权限管理
 * @author 大仙
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission,Long> {
    /**
     * 根据角色查询权限
     * @param roleId
     * @return
     */
    @Select(" select p.* from role r " +
            " LEFT JOIN role_permission_rel rpr ON r.ID = rpr.ROLE_ID " +
            " LEFT JOIN permission p ON rpr.PERMISSION_ID = p.ID " +
            " where r.ID = #{roleId} AND p.STATUS = 1 ")
    List<Permission> getPermissionByRole(@Param("roleId") Long roleId);
    /**
     * 修改状态
     * @param id
     * @param status
     */
    @Update(" update permission set STATUS = #{status} where ID = #{id} ")
    void updateStatus(@Param("id") Long id,@Param("status") Integer status);

    /**
     * 查询所有的权限集合
     * @return
     */
    @Select("select * from permission where STATUS = 1 ")
    List<Permission> all();
}
