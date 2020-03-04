package com.clark.daxian.im.netty.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface AbstractHandler {
    /**
     * 存储websocket的handler，用于共享
     */
    Map<String, WebSocketServerHandshaker> webSocketHandshakerMap =  new ConcurrentHashMap();
    /**
     * 在线人数
     */
    Map<String, ChannelHandlerContext> onlineUserMap = new ConcurrentHashMap();

    /**
     * 发送错误信息
     * @param ctx
     * @param errorMsg
     */
    default void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg,Integer code,Object data) {
        JSONObject result = new JSONObject();
        result.put("code",code==null?0:code);
        result.put("msg",errorMsg);
        result.put("data",data);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(result.toJSONString()));
    }

    /**
     * 发送给个人
     * @param toUser
     * @return
     */
    default Boolean sendMessage(Long toUser){
        ChannelHandlerContext toCtx = onlineUserMap.get(toUser);
        return false;
    }

    /**
     * 发送给群组所有在线的人
     * @param groupId
     * @return
     */
    default Boolean sendGroupMessage(Long groupId){
        return false;
    }

    /**
     * 离线退出
     * @param ctx
     */
    default void exit(ChannelHandlerContext ctx){

    }
}
