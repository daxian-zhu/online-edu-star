package com.clark.daxian.im.controller;

import com.clark.daxian.api.mq.ProducerService;
import com.clark.daxian.api.response.ComResponse;
import com.clark.daxian.im.entity.WsMessage;
import com.clark.daxian.im.netty.handler.AbstractHandler;
import com.clark.daxian.mq.config.RabbitConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "发送消息")
@Slf4j
public class SendController implements AbstractHandler {

    @Autowired
    private ProducerService producerService;

    /**
     * 发送消息
     * @param wsMessage
     * @return
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送消息")
    public ComResponse senMessage(@RequestBody WsMessage wsMessage){
        Boolean result = sendMessage(wsMessage.getTo(),wsMessage.getContent());
        if(!result){
            producerService.sendMsg(wsMessage, RabbitConfig.WEBSOCKET_EX,null);
            log.info("广播出去");
        }
        return ComResponse.successResponse();
    }
}
