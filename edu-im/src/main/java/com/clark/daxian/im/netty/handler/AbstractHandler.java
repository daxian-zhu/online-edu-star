package com.clark.daxian.im.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.clark.daxian.api.response.ComResponse;
import com.clark.daxian.im.entity.WsMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface AbstractHandler {

    String USERID="userId";
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
    default Boolean sendMessage(Long toUser, Object message){
        if(toUser==null||message==null){
            return false;
        }
        ChannelHandlerContext ctx = onlineUserMap.get(String.valueOf(toUser));
        if(ctx==null){
            return false;
        }
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(ComResponse.successResponse(message))));
        return true;
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
        webSocketHandshakerMap.remove(ctx.channel().id().asLongText());
        Long userId = Long.valueOf(ctx.channel().attr(AttributeKey.valueOf(AbstractHandler.USERID)).get().toString());
        onlineUserMap.remove(userId);
    }
}
