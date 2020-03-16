package com.clark.daxian.user.controller;

import com.clark.daxian.api.response.ComResponse;
import com.clark.daxian.dto.user.UserRequest;
import com.clark.daxian.entity.user_center.User;
import com.clark.daxian.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "用户管理")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 邮箱
     * @param email
     * @return
     */
    @GetMapping("/email/{email}")
    @ApiOperation(value = "通过主键获取用户")
    public ComResponse<User> getUserByEmail(@PathVariable("email") String email){
        return ComResponse.successResponse(userService.getByEmail(email));
    }

    /**
     * 电话
     * @param telephone
     * @return
     */
    @GetMapping("/tel/{telephone}")
    @ApiOperation(value = "通过主键获取用户")
    public ComResponse<User> getUserByTel(@PathVariable("telephone") String telephone){
        return ComResponse.successResponse(userService.getByTel(telephone));
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加用户")
    public ComResponse saveUser(@RequestBody User user){
        return ComResponse.successResponse(userService.insertUser(user));
    }

    /**
     * 用户列表
     * @param userRequest
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "用户列表")
    public ComResponse userList(UserRequest userRequest){
        return ComResponse.successResponse(userService.userList(userRequest));
    }
}
