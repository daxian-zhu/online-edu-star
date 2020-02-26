package com.clark.daxian.user.controller;

import com.clark.daxian.api.response.ComResponse;
import com.clark.daxian.entity.user_center.Role;
import com.clark.daxian.user.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理
 * @author 大仙
 */
@RestController
@Api(tags = "角色管理")
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 保存角色
     * @param role
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存角色")
    public ComResponse saveRole(@RequestBody Role role){
        return ComResponse.successResponse(roleService.save(role));
    }

    /**
     * 通过用户查询角色
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    @ApiOperation(value = "查询角色列表")
    public ComResponse listByUser(@PathVariable("userId") Long userId){
        return ComResponse.successResponse(roleService.getRoleByUser(userId));
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除角色")
    public ComResponse deleteRole(@PathVariable("id") Long id){
        roleService.deleteRole(id);
        return ComResponse.successResponse();
    }

    /**
     * 修改状态
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/{id}/{status}")
    @ApiOperation(value = "修改状态")
    public ComResponse updateRoleStatus(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        roleService.updateStatus(id,status);
        return ComResponse.successResponse();
    }
}
