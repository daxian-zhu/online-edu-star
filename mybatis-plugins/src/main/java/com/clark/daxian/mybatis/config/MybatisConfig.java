package com.clark.daxian.mybatis.config;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * 重写mybatis的configureation
 * @author 大仙
 */
public class MybatisConfig extends Configuration {

    protected final MapperRegistry mapperRegistry;

    public MybatisConfig(){
        super();
        this.mapperRegistry =  new MybatisMapperRegistry(this);
        this.mapUnderscoreToCamelCase = true;
    }

    @Override
    public MapperRegistry getMapperRegistry() {
        return this.mapperRegistry;
    }
    @Override
    public void addMappers(String packageName, Class<?> superType) {
        this.mapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        this.mapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        this.mapperRegistry.addMapper(type);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.mapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return this.mapperRegistry.hasMapper(type);
    }
}
