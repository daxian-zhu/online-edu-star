package com.clark.daxian.listener;

import com.clark.daxian.api.mq.entity.MqMessage;
import com.clark.daxian.entity.User;
import com.clark.daxian.mq.config.RabbitConfig;
import com.clark.daxian.mq.listener.DefaultListener;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class TestDirectListener extends DefaultListener<User> {

    @Override
    protected void execute(User content) throws Exception {
        log.info("执行内容"+content.toString());
    }

    @Override
    protected void failExecute(User content) {
        log.info("失败处理"+content.toString());
    }

    @RabbitListener(queues=RabbitConfig.TEST_QUEUE)
    @Override
    protected void receiveMessage(Message message, Channel channel) throws IOException{
        super.receiveMessage(message,channel);
    }
}
