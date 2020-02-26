package com.clark.daxian.mq.listener;

import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.api.mq.entity.MqMessage;
import com.clark.daxian.mq.exception.MqException;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 消费者
 * @author 大仙
 */
public abstract class DefaultListener<T extends MqMessage> {


    protected static Logger logger = LoggerFactory.getLogger(DefaultListener.class);


    private static final SerializerMessageConverter SERIALIZER_MESSAGE_CONVERTER = new SerializerMessageConverter();

    private static final String ENCODING = Charset.defaultCharset().name();

    public  static final String PARENT_MESSAGE_CLASS = "com.clark.daxian.api.mq.entity.MqMessage";


    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 业务执行方法
     * @param content
     */
    protected abstract void execute(T content)throws Exception;

    /**
     * 失败执行
     * @param content
     */
    protected abstract void failExecute(T content);

    /**
     * 接收到的消息处理
     * @param message
     * @param channel
     * @throws IOException
     */
    protected void receiveMessage(Message message, Channel channel) throws IOException{
        /**
         * 防止重复消费，可以根据传过来的唯一ID先判断缓存数据库中是否有数据
         * 1、有数据则不消费，直接应答处理
         * 2、缓存没有数据，则进行消费处理数据，处理完后手动应答
         * 3、如果消息处理异常则，可以存入数据库中，手动处理（可以增加短信和邮件提醒功能）
         */
        try{
            T content = getContent(message);
            //已经消费，直接返回
            if(canConsume(content,message.getMessageProperties().getConsumerQueue())){
                logger.info(message.getMessageProperties().getConsumerQueue()+"已经消费过");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else{
                //消费当前消息
                execute(content);
                logger.info(message.getMessageProperties().getConsumerQueue()+"消费成功");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                //消费成功删除key
                //单个消息控制
                String redisCountKey = "retry" + message.getMessageProperties().getConsumerQueue() + content.getId();
                //队列控制
                String queueKey = "retry" + message.getMessageProperties().getConsumerQueue();
                redisTemplate.delete(redisCountKey);
                redisTemplate.delete(queueKey);
            }
        }catch (Exception e){
            e.printStackTrace();
             try {
                if(dealFailAck(message,channel)){
                    logger.info("回归队列："+message);
                }else{
                    logger.error("消费失败："+message);
                    failExecute(getContent(message));
                }
            }catch (Exception e1){
                //扔掉数据
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
                logger.error("重试消费失败："+message);
                failExecute(getContent(message));
            }
        }
    }

    /**
     *
     * @param message
     * @return
     */
    private T getContent(Message message) {
        String body = getBodyContentAsString(message);
        Class<T> contentClass = null;
        try {
            contentClass = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }catch (Exception e){
            throw new MqException("缺失泛型");
        }
        if(contentClass!=null&&contentClass.getName().equals(PARENT_MESSAGE_CLASS)){
            throw new MqException("请指定相应的消息类型");
        }
        T content = JSONObject.toJavaObject(JSONObject.parseObject(body),contentClass);
        return content;
    }
    /**
     * 获取message的body
     * @param message
     * @return
     */
    private String getBodyContentAsString(Message message) {
        if (message.getBody() == null) {
            return null;
        }
        try {
            String contentType = (message.getMessageProperties() != null) ? message.getMessageProperties().getContentType() : null;
            if (MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT.equals(contentType)) {
                return SERIALIZER_MESSAGE_CONVERTER.fromMessage(message).toString();
            }
            if (MessageProperties.CONTENT_TYPE_TEXT_PLAIN.equals(contentType)
                    || MessageProperties.CONTENT_TYPE_JSON.equals(contentType)
                    || MessageProperties.CONTENT_TYPE_JSON_ALT.equals(contentType)
                    || MessageProperties.CONTENT_TYPE_XML.equals(contentType)) {
                return new String(message.getBody(), ENCODING);
            }
        }
        catch (Exception e) {
            // ignore
        }
        // Comes out as '[B@....b' (so harmless)
        return message.getBody().toString() + "(byte[" + message.getBody().length + "])";
    }

    /**
     * 失败ACK
     * @param message
     * @param channel
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private Boolean  dealFailAck(Message message,Channel channel) throws IOException, InterruptedException{
        T content = getContent(message);
        //单个消息控制
        String redisCountKey = "retry"+message.getMessageProperties().getConsumerQueue()+content.getId();
        String retryCount = redisTemplate.opsForValue().get(redisCountKey);
        long basic = 1000L;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //队列控制
        String queueKey = "retry"+message.getMessageProperties().getConsumerQueue();
        //没有重试过一次
        if(StringUtils.isBlank(retryCount)){
            if(!redisTemplate.opsForValue().setIfAbsent(queueKey,"lock",25,TimeUnit.SECONDS)) {
                channel.basicNack(deliveryTag,false,true);
                logger.info("deliveryTag:"+deliveryTag);
                return true;
            }
            //预防队列太长，造成延迟时间过长
            redisTemplate.opsForValue().setIfAbsent(redisCountKey,"1",5,TimeUnit.MINUTES);
            logger.info("开始第一次尝试：");
        }else{
            switch (Integer.valueOf(retryCount)){
                case 1:
                    redisTemplate.opsForValue().set(redisCountKey,"2");
                    logger.info("开始第二次尝试：");
                    break;
                case 2:
                    redisTemplate.opsForValue().set(redisCountKey,"3");
                    logger.info("开始第三次尝试：");
                    break;
                case 3:
                    redisTemplate.opsForValue().set(redisCountKey,"4");
                    logger.info("开始第四次尝试：");
                    break;
                default:
                    //扔掉消息，准备持久化
                    redisTemplate.delete(redisCountKey);
                    redisTemplate.delete(queueKey);
                    channel.basicNack(deliveryTag,false,false);
                    return false;
            }
        }
        channel.basicNack(deliveryTag,false,true);
        return true;
    }

    /**
     * 是否能消费，防止重复消费
     * @param content
     * @param queueName
     * @return
     */
    private Boolean canConsume(T content,String queueName) {
        if(redisTemplate.opsForValue().get(queueName+":"+content.getId())==null){
            return false;
        }else{
            //存储消费标志
            redisTemplate.opsForValue().set(queueName+":"+content.getId(),"lock",30, TimeUnit.SECONDS);
            return true;
        }
    }
}