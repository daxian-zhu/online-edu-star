package com.clark.daxian.im.mq;


import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.im.entity.WsMessage;
import com.clark.daxian.im.netty.handler.AbstractHandler;
import com.clark.daxian.im.util.IPUtil;
import com.clark.daxian.mq.listener.DefaultListener;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * websocket监听，实现websocket的共享
 * @author 大仙
 */
@Component
@Slf4j
public class WebSocketListener extends DefaultListener<WsMessage> implements AbstractHandler {


    private final String QUEUE_NAME="websocket."+ IPUtil.getServerIp();

    @Autowired
    private FanoutExchange websocketFanoutExchange;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME,true,false,false);
    }

    /**
     * 绑定
     * @return
     */
    @Bean
    public Binding bindingFanoutOrderQueue() {
        return BindingBuilder.bind(queue()).to(websocketFanoutExchange);
    }

    /**
     * 监听
     * @param message
     * @param channel
     * @throws IOException
     */
    @Override
    @RabbitListener(queues = "#{queue.name}")
    protected void receiveMessage(Message message, Channel channel) throws IOException {
        super.receiveMessage(message,channel);
    }

    @Override
    protected void execute(WsMessage content) throws Exception {
        log.info("开始处理消息");
        if(StringUtils.isNotBlank(redisTemplate.opsForValue().get(content.getId()))){
            logger.error("重复消费："+ JSONObject.toJSONString(content));
            return;
        }else{
            redisTemplate.opsForValue().setIfAbsent(content.getId(),"lock",5, TimeUnit.SECONDS);
        }
        logger.info("消息内容:"+ JSONObject.toJSONString(content));
        Boolean result = sendMessage(content.getTo(),content.getContent());
        if(result){
            logger.info("发送消息成功");
        }else{
            logger.info("发送失败");
        }
    }

    @Override
    protected void failExecute(WsMessage content) {
        logger.error("发送消息失败："+JSONObject.toJSONString(content));
    }
}
