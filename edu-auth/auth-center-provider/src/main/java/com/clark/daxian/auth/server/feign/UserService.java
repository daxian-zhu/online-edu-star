package com.clark.daxian.auth.server.feign;

import com.clark.daxian.api.response.ComResponse;
import com.clark.daxian.entity.user_center.Permission;
import com.clark.daxian.entity.user_center.Role;
import com.clark.daxian.entity.user_center.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 用户业务接口
 * @author 大仙
 */
@FeignClient(name = "user")
public interface UserService {
    /**
     * 通过用户名，或者email获取用户名
     * @param email
     * @return
     */
    @GetMapping("/user/email/{email}")
    ComResponse<User> getUserByEmail(@PathVariable("email") String email);

    /**
     * 通过用户名，或者email获取用户名
     * @param telephone
     * @return
     */
    @GetMapping("/user/tel/{telephone}")
    ComResponse<User> getUserByTel(@PathVariable("telephone") String telephone);

    /**
     * 根据用户查询角色
     * @param userId
     * @return
     */
    @GetMapping("/role/list/{userId}")
    ComResponse<List<Role>> listByUser(@PathVariable("userId") Long userId);
    /**
     * 通过用户查询权限
     * @param roleId
     * @return
     */
    @GetMapping("/permission/list/{roleId}")
    ComResponse<List<Permission>> listByRole(@PathVariable("roleId") Long roleId);
}
