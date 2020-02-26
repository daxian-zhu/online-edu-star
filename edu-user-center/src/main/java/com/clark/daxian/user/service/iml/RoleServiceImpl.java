package com.clark.daxian.user.service.iml;

import com.clark.daxian.entity.user_center.Role;
import com.clark.daxian.user.exception.UserException;
import com.clark.daxian.user.mapper.RoleMapper;
import com.clark.daxian.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色业务接口实现
 * @author 大仙
 */
@Service
public class RoleServiceImpl implements RoleService {


    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleByUser(Long userId) {
        if(userId==null){
            throw new UserException("缺失请求参数");
        }
        return roleMapper.getRoleByUser(userId);
    }

    @Override
    public Long save(Role role) {
        try{
            roleMapper.insert(role);
        }catch (Exception e){
            e.printStackTrace();
            throw new UserException("添加角色失败");
        }
        return role.getId();
    }

    @Override
    public void deleteRole(Long id) {
        if(id==null){
            throw new UserException("缺失请求参数");
        }
        roleMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if(id==null||status==null){
            throw new UserException("缺失请求参数");
        }
        roleMapper.updateStatus(id,status);
    }
}
