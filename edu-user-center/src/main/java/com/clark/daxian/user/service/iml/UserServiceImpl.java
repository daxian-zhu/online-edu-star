package com.clark.daxian.user.service.iml;

import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.dto.user.UserRequest;
import com.clark.daxian.entity.user_center.User;
import com.clark.daxian.user.exception.UserException;
import com.clark.daxian.user.mapper.UserMapper;
import com.clark.daxian.user.password.BCryptPasswordEncoder;
import com.clark.daxian.user.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户业务实现
 * @author 大仙
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Long insertUser(User user) {
        if(StringUtils.isBlank(user.getEmail())&&StringUtils.isBlank(user.getTelephone())){
            throw new UserException("缺失请求参数");
        }
        try {
            //设置初始密码
            if(StringUtils.isBlank(user.getPassword())){
                user.setPassword(Constant.INIT_PWD);
            }
            //加密
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userMapper.insert(user);
        }catch (Exception e){
            e.printStackTrace();
            throw new UserException("添加用户失败");
        }
        return user.getId();
    }

    @Override
    public User getById(Long id) {
        if(id==null){
            throw new UserException("缺失请求参数");
        }
        return userMapper.getById(id);
    }

    @Override
    public User getByTel(String telephone) {
        if(StringUtils.isBlank(telephone)){
            throw new UserException("缺失请求参数");
        }
        return userMapper.getByTel(telephone);
    }

    @Override
    public User getByEmail(String email) {
        if(StringUtils.isBlank(email)){
            throw new UserException("缺失请求参数");
        }
        return userMapper.getByEmail(email);
    }

    @Override
    public PageInfo<User> userList(UserRequest params) {
        Integer page = params.getPage();
        Integer pageSize = params.getPageSize();
        //分页处理
        PageHelper.startPage(page, pageSize, true);
        List<User> classTimeList = userMapper.userList(params);
        PageInfo<User> pageInfo = new PageInfo<>(classTimeList);
        return pageInfo;
    }
}
