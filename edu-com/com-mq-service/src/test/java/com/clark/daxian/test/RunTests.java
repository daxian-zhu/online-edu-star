package com.clark.daxian.test;

import com.clark.daxian.Run;
import com.clark.daxian.api.mq.ProducerService;
import com.clark.daxian.entity.User;
import com.clark.daxian.mq.annotation.EnableRabbitMq;
import com.clark.daxian.mq.config.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Run.class)
@EnableAutoConfiguration
@EnableRabbitMq
public class RunTests {

    @Autowired
    private ProducerService producerService;

    @Test
    public void test() throws Exception {
        User user = new User();
        user.setId("1");
        user.setName("张三");
        producerService.sendMsg(user, RabbitConfig.TEST_EXCHANGE,RabbitConfig.TEST_ROUTINGKEY);
    }

}
