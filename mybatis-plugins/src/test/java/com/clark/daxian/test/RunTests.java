package com.clark.daxian.test;

import com.clark.daxian.Run;
import com.clark.daxian.entity.User;
import com.clark.daxian.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Run.class)
@EnableAutoConfiguration
public class RunTests {

    @Resource
    private UserMapper userMapper;


    @Test
    public void test() throws Exception {

        User user = null;

        // 新增测试
        System.out.println("------------ 新增测试 ------------");
        user = new User();
        user.setNameBig("conanli");
        user.setTelephone(String.valueOf(Math.random()));
        System.out.println("insert: " + userMapper.insert(user));

        // 更新测试
        System.out.println("------------ 更新测试 ------------");
        user = new User();
        user.setId(1L);
        user.setTelephone("111111");
        System.out.println("update: " + userMapper.updateById(user));

        // 获取测试
        System.out.println("------------ 获取测试 ------------");
        System.out.println("user: " + userMapper.getById(1L));

        // 删除测试
        System.out.println("------------ 删除测试 ------------");
        System.out.println("delete: " + userMapper.deleteById(1L));

        // 存在测试
        System.out.println("------------ 存在测试 ------------");
        System.out.println("exist: " + userMapper.existById(1L));

        System.out.println("all"+userMapper.getAll());

    }
}
