package com.clark.daxian.auth.resource.util;

import com.alibaba.fastjson.JSONArray;
import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.entity.user_center.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限工具类
 * @author 大仙
 */
public class PermissionUtil {

    @Autowired
    private RedisTemplate<String, Permission> permissionRedisTemplate;

    /**
     * 根据角色获取权限列表
     * @param array
     * @return
     */
    public List<Permission> getResultPermission(JSONArray array){
        //查询全部的权限
        List<Permission> allPermissions = allPermissions();
        List<Permission> result = new ArrayList<>();
        for(int i = 0;i<array.size();i++){
            String roleCode = array.getString(i);
            List<Permission> permissions = getPermissions(roleCode);
            result.addAll(getAllChild(permissions,allPermissions,null));
        }
        if(result.size()>0){
            result = result.stream().distinct().collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 根据角色获取所有的权限
     * @param roleCode
     * @return
     */
    public List<Permission> getResultPermission(String  roleCode){
        //查询全部的权限
        List<Permission> allPermissions = allPermissions();
        List<Permission> result = new ArrayList<>();
        List<Permission> permissions = getPermissions(roleCode);
        result.addAll(getAllChild(permissions,allPermissions,null));
        if(result.size()>0){
            result = result.stream().distinct().collect(Collectors.toList());
        }
        return result;
    }
    /**
     * 获取当前用户的权限集合
     * @param authority
     * @return
     */
    private List<Permission> getPermissions(String authority){
        String redisKey = Constant.PERMISSIONS+authority;
        long size = permissionRedisTemplate.opsForList().size(redisKey);
        List<Permission> permissions = permissionRedisTemplate.opsForList().range(redisKey, 0, size);
        return permissions;
    }
    /**
     * 获得所有的子权限
     * @param permissions
     * @param allPermissions
     * @param result
     * @return
     */
    private List<Permission> getAllChild(List<Permission> permissions,List<Permission> allPermissions,List<Permission> result){
        //结果集
        if(result==null){
            result = new ArrayList<>();
            result.addAll(permissions);
        }
        List<Permission> needFindSub = new ArrayList<>();
        for(Permission permission:permissions) {
            //得到儿子
            List<Permission> subPer =  allPermissions
                    .stream()
                    .filter(desPer->permission.getId().equals(desPer.getParentPermission())).collect(Collectors.toList());
            //去除儿子中的重复数据
            for (Permission sub : subPer) {
                //如果重复，去除
                if(result.stream().anyMatch(p->p.getId().equals(sub.getId()))){
                    continue;
                }
                result.add(sub);
                needFindSub.add(sub);
            }
        }
        if(needFindSub.size()>0) {
            return getAllChild(needFindSub, allPermissions, result);
        }
        return result;
    }
    /**
     * 获取所有的权限
     * @return
     */
    private List<Permission> allPermissions(){
        String redisKey = Constant.PERMISSIONS+Constant.ALL;
        long size = permissionRedisTemplate.opsForList().size(redisKey);
        List<Permission> permissions = permissionRedisTemplate.opsForList().range(redisKey, 0, size);
        return permissions;
    }

}
