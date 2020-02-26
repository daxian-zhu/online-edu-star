package com.clark.daxian.mybatis.mapper;

import com.clark.daxian.mybatis.driver.BaseMapperDriver;
import org.apache.ibatis.annotations.*;

/**
 * 基础base
 * @param <T>
 * @param <K>
 */
public interface BaseMapper<T, K> {
    /**
     * 插入
     * @param model
     * @return
     */
    @Lang(BaseMapperDriver.class)
    @Insert({"<script>", "INSERT INTO ${table} ${values}", "</script>"})
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Long insert(T model);

    /**
     * 修改
     * @param model
     * @return
     */
    @Lang(BaseMapperDriver.class)
    @Update({"<script>", "UPDATE ${table} ${sets} WHERE ${id}=#{id}", "</script>"})
    Long updateById(T model);

    /**
     * 删除
     * @param id
     * @return
     */
    @Lang(BaseMapperDriver.class)
    @Delete("DELETE FROM ${table} WHERE ${id}=#{id}")
    Long deleteById(@Param("id") K id);

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    @Lang(BaseMapperDriver.class)
    @Select("SELECT * FROM ${table} WHERE ${id}=#{id}")
    T getById(@Param("id") K id);

    /**
     * 判断是否存在
     * @param id
     * @return
     */
    @Lang(BaseMapperDriver.class)
    @Select("SELECT COUNT(1) FROM ${table} WHERE ${id}=#{id}")
    Boolean existById(@Param("id") K id);
}