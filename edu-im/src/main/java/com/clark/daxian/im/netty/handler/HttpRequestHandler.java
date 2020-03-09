package com.clark.daxian.im.netty.handler;

import com.clark.daxian.auth.api.entity.BaseUser;
import com.clark.daxian.auth.api.util.TokenUtil;
import com.clark.daxian.im.exception.ImException;
import com.clark.daxian.im.properties.ImProperties;
import com.clark.daxian.im.service.TokenCheckService;
import com.clark.daxian.im.util.UrlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Sharable
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<Object> implements AbstractHandler {

    @Autowired
    private ImProperties imProperties;

    @Autowired
    private TokenCheckService tokenCheckService;
    /**
     * 描述：处理Http请求，主要是完成HTTP协议到Websocket协议的升级
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        String uri = req.uri();
        log.info("地址："+uri);
        String token = UrlUtil.getParam(uri,"token");
        if(StringUtils.isBlank(token)){
            sendErrorMessage(ctx,"无权限",403,null);
            throw new ImException("无权限");
        }
        //token鉴权
        if(!tokenCheckService.checkTokenValid(token)){
            sendErrorMessage(ctx,"无权限",403,null);
            throw new ImException("无权限");
        }
        //处理连接地址，如果需要针对路由进行分发，可以进行处理
        String onlyUrl =UrlUtil.getUrl(uri);
        if(!onlyUrl.equals("/web/chat")){
            ctx.channel().attr(AttributeKey.valueOf(AbstractHandler.URL_TYPE)).set("WEB_CHAT");
            throw new ImException("确认路由地址是否正确");
        }
        //获取Token
        BaseUser baseUser = TokenUtil.getBaseUserByToken(token);
        //没发现下面的URL有什么用
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                imProperties.getWebsocketUrl(), null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
        //缓存用户信息
        ctx.channel().attr(AttributeKey.valueOf(AbstractHandler.USER_ID)).set(baseUser.getId());
        //处理连接
        dealConnect(baseUser,ctx,handshaker);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    /**
     * 处理连接
     * @param baseUser
     * @param ctx
     * @param handshaker
     */
    private void dealConnect(BaseUser baseUser,ChannelHandlerContext ctx,WebSocketServerHandshaker handshaker){
        onlineUserMap.put(String.valueOf(baseUser.getId()),ctx);
        webSocketHandshakerMap.put(ctx.channel().id().asLongText(),handshaker);
    }
    /**
     * 响应给客户端
     * @param ctx
     * @param req
     * @param res
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        //5.x版本是HttpHeaderUtil
        boolean keepAlive = HttpUtil.isKeepAlive(req);
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 描述：异常处理，关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            ctx.fireChannelRead(((WebSocketFrame) msg).retain());
        }
    }
}
