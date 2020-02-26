package com.clark.daxian.user.service.iml;

import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.entity.user_center.Permission;
import com.clark.daxian.user.exception.UserException;
import com.clark.daxian.user.mapper.PermissionMapper;
import com.clark.daxian.user.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限业务接口实现
 * @author 大仙
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    public RedisTemplate<String, Permission> permissionRedisTemplate;

    @Override
    public List<Permission> getPermissionByRole(Long roleId) {
        if(roleId==null){
            throw new UserException("缺失请求参数");
        }
        return permissionMapper.getPermissionByRole(roleId);
    }

    @Override
    public Long save(Permission permission) {
        try{
            permissionMapper.insert(permission);
        }catch (Exception e){
            e.printStackTrace();
            throw new UserException("添加权限失败");
        }
        //更新缓存
        all();
        return permission.getId();
    }

    @Override
    public void deletePermission(Long id) {
        if(id==null){
            throw new UserException("缺失请求参数");
        }
        permissionMapper.deleteById(id);
        //更新缓存
        all();
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if(id==null||status==null){
            throw new UserException("缺失请求参数");
        }
        permissionMapper.updateStatus(id,status);
        //更新缓存
        all();
    }

    @Override
    public List<Permission> all() {
        permissionRedisTemplate.delete(Constant.PERMISSIONS+Constant.ALL);
        List<Permission> permissions = permissionMapper.all();
        permissions.forEach(permission -> {
            permissionRedisTemplate.opsForList().rightPush(Constant.PERMISSIONS+Constant.ALL,permission);
        });
        return permissions;
    }
}
