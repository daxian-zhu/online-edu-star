package com.clark.daxian.user.controller;

import com.clark.daxian.api.response.ComResponse;
import com.clark.daxian.entity.user_center.Permission;
import com.clark.daxian.user.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理
 * @author 大仙
 */
@RestController
@Api(tags = "权限管理")
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 保存权限
     * @param permission
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存权限")
    public ComResponse savPermission(@RequestBody Permission permission){
        return ComResponse.successResponse(permissionService.save(permission));
    }

    /**
     * 通过用户查询权限
     * @param roleId
     * @return
     */
    @GetMapping("/list/{roleId}")
    @ApiOperation(value = "查询权限列表")
    public ComResponse listByRole(@PathVariable("roleId") Long roleId){
        return ComResponse.successResponse(permissionService.getPermissionByRole(roleId));
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除权限")
    public ComResponse deleteRole(@PathVariable("id") Long id){
        permissionService.deletePermission(id);
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
        permissionService.updateStatus(id,status);
        return ComResponse.successResponse();
    }

    /**
     * 权限集合
     * 用户刷新缓存
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "所有的权限列表")
    public ComResponse allPermission(){
        return ComResponse.successResponse(permissionService.all());
    }
}
