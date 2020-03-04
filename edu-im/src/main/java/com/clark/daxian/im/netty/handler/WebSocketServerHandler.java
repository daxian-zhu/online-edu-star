package com.clark.daxian.im.netty.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> implements AbstractHandler{


    /**
     * 描述：处理WebSocketFrame
     * @param ctx
     * @param frame
     * @throws Exception
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // 关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker webSocketServerHandshaker =  webSocketHandshakerMap.get(ctx.channel().id().asLongText());
            if (webSocketServerHandshaker == null) {
                sendErrorMessage(ctx, "不存在的客户端连接！",500,null);
            } else {
                //关闭
                webSocketServerHandshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            }
            return;
        }
        // ping请求
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 只支持文本格式，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            sendErrorMessage(ctx, "仅支持文本(Text)格式，不支持二进制消息",500,null);
        }

        // 客服端发送过来的消息
        String request = ((TextWebSocketFrame)frame).text();
        log.info("服务端收到新信息：" + request);
//        JSONObject param = null;
//        try {
//            param = JSONObject.parseObject(request);
//        } catch (Exception e) {
//            sendErrorMessage(ctx, "JSON字符串转换出错！",500,null);
//            e.printStackTrace();
//            return;
//        }
//        if (param == null) {
//            sendErrorMessage(ctx, "参数为空！",500,null);
//            return;
//        }

//        String type = (String) param.get("type");
//        switch (type) {
//
//        }
    }
    /**
     * 描述：客户端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        exit(ctx);
    }
    /**
     * 异常处理：关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 这里其实我还是更喜欢5.x版本的写法
     * @param ctx
     * @param webSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) throws Exception {
        handlerWebSocketFrame(ctx,webSocketFrame);
    }
}
