package com.clark.daxian.user.mapper;


import com.clark.daxian.entity.user_center.Role;
import com.clark.daxian.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 角色管理数据层
 * @author 大仙
 */
@Mapper
public interface RoleMapper  extends BaseMapper<Role,Long> {

    /**
     * 查询角色列表
     * @param userId
     * @return
     */
    @Select("select r.* from user u" +
            " LEFT JOIN user_role_rel urr ON urr.USER_ID = u.ID " +
            " LEFT JOIN role r on urr.ROLE_ID = r.ID " +
            " where u.ID = #{userId} AND r.STATUS = 1 ")
    List<Role> getRoleByUser(@Param("userId") Long userId);

    /**
     * 变更角色
     * @param id
     * @param status
     */
    @Update("update role set STATUS = #{status} where ID = #{id}")
    void updateStatus(@Param("id") Long id,@Param("status") Integer status);
}
