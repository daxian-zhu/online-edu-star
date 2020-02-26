package com.clark.daxian.mq.config;

import com.clark.daxian.api.mq.ProducerService;
import com.clark.daxian.mq.service.impl.ProducerServiceImpl;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * @Author: 朱维
 * @Date 11:43 2019/12/10
 */
/**
 * direct直连模型
 * fanout无路由模式，使用场景广播消息
 * topic 模糊路由模式，适用业务分组
 * fanout>direct>topic 这里是多消费模式，topic和fanout都能实现，通过性能对比选择fanout  11>10>6
 */
public class RabbitConfig {

    /**
     * 初始化相关配置
     * @return
     */
    @Bean
    public RabbitTemplateConfig rabbitTemplateConfig(){
        RabbitTemplateConfig rabbitTemplateConfig = new RabbitTemplateConfig();
        return rabbitTemplateConfig;
    }

    /**
     * 提供者
     * @return
     */
    @Bean
    public ProducerService producerService(){
        ProducerServiceImpl producerService = new ProducerServiceImpl();
        return producerService;
    }

    /**
     * 测试队列名
     */
    public static final String TEST_QUEUE = "test.q";
    /**
     * 测试交换机
     */
    public static final String TEST_EXCHANGE = "test.ex";
    /**
     * 测试的路由器
     */
    public static final String TEST_ROUTINGKEY = "test.rk";

    @Bean
    public DirectExchange testDirectExchange(){
        DirectExchange directExchange = new DirectExchange(TEST_EXCHANGE);
        return directExchange;
    }

    @Bean
    public Queue testQueue(){
        Queue queue = new Queue(TEST_QUEUE);
        return queue;
    }
    /**
     * 绑定
     * @return
     */
    @Bean
    public Binding bindingTest() {
        return BindingBuilder.bind(testQueue()).to(testDirectExchange()).with(TEST_ROUTINGKEY);
    }
}

