package com.clark.daxian.test;

import com.clark.daxian.Run;
import com.clark.daxian.aliyun.annotation.EnableAliOss;
import com.clark.daxian.aliyun.annotation.EnableAliSms;
import com.clark.daxian.api.aliyun.OssService;
import com.clark.daxian.api.enums.SMSTemplate;
import com.clark.daxian.api.aliyun.SmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Run.class)
@EnableAutoConfiguration
@EnableAliOss
public class RunTests {

    @Autowired
    private OssService ossService;

    @Test
    public void test() throws Exception {
        ossService.getToken("aa.sb3");
    }
}
