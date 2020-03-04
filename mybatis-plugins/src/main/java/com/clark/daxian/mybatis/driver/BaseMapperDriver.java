package com.clark.daxian.mybatis.driver;

import com.clark.daxian.mybatis.annotation.*;
import com.clark.daxian.mybatis.config.MybatisConfig;
import com.clark.daxian.mybatis.config.MybatisMapperRegistry;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 定义自定义的语言
 * @author 大仙
 */
public class BaseMapperDriver extends XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        //获取当前mapper
        Class<?> mapperClass = null;
        if(configuration instanceof MybatisConfig){
            mapperClass = MybatisMapperRegistry.getCurrentMapper();
        }
        if(mapperClass == null){
            throw new RuntimeException("解析SQL出错");
        }
        //处理SQL
        if(mapperClass!=null) {
            Class<?>[] generics = getMapperGenerics(mapperClass);
            Class<?> modelClass = generics[0];
            Class<?> idClass = generics[1];
            //表名
            script = setTable(script, modelClass);
            //主键
            script = setId(script, modelClass);
            //插入
            script = setValues(script,modelClass);
            //修改
            script = setSets(script, modelClass);
            //IN语句
            script = setIn(script);
            //单表查询结果映射，利用别名
            script = setResultAlias(script,modelClass);
        }

        return super.createSqlSource(configuration, script, parameterType);
    }

    /**
     * 获取泛型
     * @param mapperClass
     * @return
     */
    private  Class<?>[] getMapperGenerics(Class<?> mapperClass){
        Class<?>[]  classes = new Class[2];
        Type[] types =  mapperClass.getGenericInterfaces();
        for(Type type:types){
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type[] types1 = parameterizedType.getActualTypeArguments();
            classes[0] = (Class<?>) types1[0];
            classes[1] = (Class<?>) types1[1];
        }
        return classes;
    }

    /**
     * 设置表名
     * @param script
     * @param modelClass
     * @return
     */
    private String setTable(String script, Class<?> modelClass){
        final Pattern inPattern = Pattern.compile("\\$\\{table\\}");
        Matcher matcher = inPattern.matcher(script);
        if (matcher.find()) {
            //如果注解相同
            if (modelClass.isAnnotationPresent(Table.class)) {
                script = script.replaceAll("\\$\\{table\\}", modelClass.getAnnotation(Table.class).name());
            } else {
                System.out.println("=====" + modelClass.getSimpleName());
                script = script.replaceAll("\\$\\{table\\}", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelClass.getSimpleName()));
            }
        }
        return script;
    }

    /**
     * 替换ID
     * @param script
     * @param modelClass
     * @return
     */
    private String setId(String script,Class<?> modelClass){
        final Pattern inPattern = Pattern.compile("\\$\\{id\\}");
        Matcher matcher = inPattern.matcher(script);
        if (matcher.find()) {
            boolean exitIdEnum = false;
            for (Field field : modelClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    script = script.replaceAll("\\$\\{id\\}", field.getAnnotation(Id.class).name());
                    exitIdEnum = true;
                    break;
                }
            }
            if (!exitIdEnum) {
                script = script.replaceAll("\\$\\{id\\}", "id");
            }
        }
        return script;
    }

    /**
     * 替换sets
     * @param script
     * @param modelClass
     * @return
     */
    private String setSets(String script,Class<?> modelClass){
        final Pattern inPattern = Pattern.compile("\\$\\{sets\\}");
        Matcher matcher = inPattern.matcher(script);
        if (matcher.find()) {
            StringBuffer ss = new StringBuffer();
            ss.append("<set>");
            //是否使用父类的属性
            if(modelClass.isAnnotationPresent(UserParent.class)){
                //获取父类
                Class<?> superClass = modelClass.getSuperclass();
                for(Field field : superClass.getDeclaredFields()){
                    //非public和protected的不处理
                    if(!(Modifier.isPublic(field.getModifiers())||Modifier.isProtected(field.getModifiers()))){
                        continue;
                    }
                    //如果不显示，直接返回
                    if (field.isAnnotationPresent(Invisiable.class)) {
                        continue;
                    }
                    //如果不显示，直接返回
                    if (field.isAnnotationPresent(Id.class)) {
                        continue;
                    }
                    //非驼峰命名规则
                    String temp = "<if test=\"__field != null\">__column=#{__field},</if>";
                    if(field.isAnnotationPresent(Column.class)){
                        ss.append(temp.replaceAll("__field", field.getName())
                                .replaceAll("__column",field.getAnnotation(Column.class).name() ));
                        continue;
                    }
                    //驼峰命名规则
                    ss.append(temp.replaceAll("__field", field.getName())
                            .replaceAll("__column", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName())));
                }

            }
            //本身
            for (Field field : modelClass.getDeclaredFields()) {
                //如果不显示，直接返回
                if (field.isAnnotationPresent(Invisiable.class)) {
                    continue;
                }
                //如果不显示，直接返回
                if (field.isAnnotationPresent(Id.class)) {
                    continue;
                }
                //非驼峰命名规则
                String temp = "<if test=\"__field != null\">__column=#{__field},</if>";
                if(field.isAnnotationPresent(Column.class)){
                    ss.append(temp.replaceAll("__field", field.getName())
                            .replaceAll("__column",field.getAnnotation(Column.class).name() ));
                    continue;
                }
                //驼峰命名规则
                ss.append(temp.replaceAll("__field", field.getName())
                        .replaceAll("__column", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName())));
            }

            ss.deleteCharAt(ss.lastIndexOf(","));
            ss.append("</set>");

            script = matcher.replaceAll(ss.toString());
        }
        return script;
    }

    /**
     * 设置Value
     * @param script
     * @param modelClass
     * @return
     */
    private String setValues(String script,Class<?> modelClass){
        final Pattern inPattern = Pattern.compile("\\$\\{values\\}");
        Matcher matcher = inPattern.matcher(script);
        if (matcher.find()) {
            StringBuffer ss = new StringBuffer();
            List<String> columns = new ArrayList<>();
            List<String> values = new ArrayList<>();
            //是否使用父类的属性
            if(modelClass.isAnnotationPresent(UserParent.class)){
                //获取父类
                Class<?> superClass = modelClass.getSuperclass();
                for(Field field : superClass.getDeclaredFields()){
                    //非public和protected的不处理
                    if(!(Modifier.isPublic(field.getModifiers())||Modifier.isProtected(field.getModifiers()))){
                        continue;
                    }
                    //如果不显示，直接返回
                    if (field.isAnnotationPresent(Invisiable.class)) {
                        continue;
                    }
                    //非驼峰命名规则
                    values.add("#{"+field.getName()+"}");
                    if(field.isAnnotationPresent(Column.class)){
                        columns.add(field.getAnnotation(Column.class).name() );
                    }else {
                        //驼峰命名规则
                        columns.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()));
                    }
                }

            }
            //自身
            for (Field field : modelClass.getDeclaredFields()) {
                //如果不显示，直接返回
                if (field.isAnnotationPresent(Invisiable.class)) {
                    continue;
                }
                //非驼峰命名规则
                values.add("#{"+field.getName()+"}");
                if(field.isAnnotationPresent(Column.class)){
                    columns.add(field.getAnnotation(Column.class).name() );
                }else {
                    //驼峰命名规则
                    columns.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()));
                }
            }
            ss.append("("+ StringUtils.join(columns.toArray(),",") +") VALUES ("+ StringUtils.join(values.toArray(),",")+")");
            script = matcher.replaceAll(ss.toString());
        }
        return script;
    }

    /**
     * in语句
     * @param script
     * @return
     */
    private String setIn(String script){
        final Pattern inPattern = Pattern.compile("\\$\\{ins\\}");
        Matcher matcher = inPattern.matcher(script);
        if (matcher.find()) {
           script = matcher.replaceAll("(<foreach collection=\"$1\" item=\"__item\" separator=\",\" >#{__item}</foreach>)");
        }
        return script;
    }


    private String setResultAlias(String script,Class<?> modelClass){

        return script;
    }
}