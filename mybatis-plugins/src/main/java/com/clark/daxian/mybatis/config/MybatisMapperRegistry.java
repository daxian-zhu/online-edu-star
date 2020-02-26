package com.clark.daxian.mybatis.config;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

/**
 * 自定义mapperRegistry
 * @author 大仙
 */
public class MybatisMapperRegistry extends MapperRegistry {


    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap();

    private Configuration config;

    private static Class<?> currentMapper;

    public MybatisMapperRegistry(Configuration config) {
        super(config);
        this.config = config;
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory)this.knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        } else {
            try {
                return mapperProxyFactory.newInstance(sqlSession);
            } catch (Exception var5) {
                throw new BindingException("Error getting mapper instance. Cause: " + var5, var5);
            }
        }
    }

    @Override
    public <T> boolean hasMapper(Class<T> type) {
        return this.knownMappers.containsKey(type);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (this.hasMapper(type)) {
                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
            }

            boolean loadCompleted = false;

            try {
                this.knownMappers.put(type, new MapperProxyFactory(type));
                MapperAnnotationBuilder parser = new MapperAnnotationBuilder(this.config, type);
                currentMapper = type;
                parser.parse();
                currentMapper=null;
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    this.knownMappers.remove(type);
                }

            }
        }

    }

    @Override
    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(this.knownMappers.keySet());
    }


    @Override
    public void addMappers(String packageName, Class<?> superType) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        Iterator var5 = mapperSet.iterator();

        while(var5.hasNext()) {
            Class<?> mapperClass = (Class)var5.next();
            this.addMapper(mapperClass);
        }

    }

    @Override
    public void addMappers(String packageName) {
        this.addMappers(packageName, Object.class);
    }


    public static Class<?> getCurrentMapper() {
        return currentMapper;
    }
}
