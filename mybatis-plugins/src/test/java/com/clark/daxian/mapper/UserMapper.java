package com.clark.daxian.mapper;

import com.clark.daxian.entity.User;
import com.clark.daxian.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User,Long> {


    @Select("select * from user ")
    List<User> getAll();
}
