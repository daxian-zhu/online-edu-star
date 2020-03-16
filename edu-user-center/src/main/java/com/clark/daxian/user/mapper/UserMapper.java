package com.clark.daxian.user.mapper;

import com.clark.daxian.dto.user.UserRequest;
import com.clark.daxian.entity.user_center.User;
import com.clark.daxian.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据接口
 * @author 大仙
 */
@Mapper
public interface UserMapper extends BaseMapper<User,Long> {

    /**
     * 根据电话号码查询用户
     * @param telephone
     * @return
     */
    @Select("select * from user where TELEPHONE = #{telephone}")
    User getByTel(@Param("telephone") String telephone);

    /**
     * 根据邮箱查询用户
     * @param email
     * @return
     */
    @Select("select * from user where EMAIL = #{email}")
    User getByEmail(@Param("email")String email);

    /**
     * 查询用户列表
     * @param params
     * @return
     */
    @Select("<script> " +
            "select * from user where 1 = 1 "+
            "<if test=\"params.email!=null\"> AND email like CONCAT('%',#{params.email},'%') </if>" +
            "<if test=\"params.telephone!=null\"> AND telephone = #{params.telephone} </if>" +
            "</script> ")
    List<User> userList(@Param("params") UserRequest params);
}
