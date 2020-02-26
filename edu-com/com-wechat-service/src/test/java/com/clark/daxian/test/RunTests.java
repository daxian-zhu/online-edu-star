package com.clark.daxian.test;

import com.clark.daxian.Run;
import com.clark.daxian.wechat.annotation.EnableWeChat;
import com.clark.daxian.api.wechat.WeChatComService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Run.class)
@EnableAutoConfiguration
@EnableWeChat
public class RunTests {

    @Autowired
    private WeChatComService weChatComService;


    @Test
    public void test() throws Exception {
        System.out.println(weChatComService.getAccessToken());
    }

}
