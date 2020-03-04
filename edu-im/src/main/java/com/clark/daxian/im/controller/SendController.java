package com.clark.daxian.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.api.entity.Constant;
import com.clark.daxian.api.mq.ProducerService;
import com.clark.daxian.api.response.ComResponse;
import com.clark.daxian.auth.api.entity.BaseUser;
import com.clark.daxian.auth.api.util.TokenUtil;
import com.clark.daxian.im.entity.WsMessage;
import com.clark.daxian.im.netty.handler.HttpRequestHandler;
import com.clark.daxian.mq.config.RabbitConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "发送消息")
public class SendController {

    @Autowired
    private ProducerService producerService;

    @Autowired
    public RedisTemplate<String, ChannelHandlerContext> handlerContextRedisTemplate;
    /**
     * 发送消息
     * @param wsMessage
     * @return
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送消息")
    public ComResponse senMessage(@RequestBody WsMessage wsMessage){
        BaseUser user = TokenUtil.getUser();
//        producerService.sendMsg(wsMessage, RabbitConfig.WEBSOCKET_EX,null);
        ChannelHandlerContext ctx = handlerContextRedisTemplate.opsForValue().get(Constant.KEYPER+user.getId());
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(wsMessage)));
        return ComResponse.successResponse();
    }
}
