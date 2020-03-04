package com.clark.daxian.im.netty.handler;

import com.clark.daxian.im.properties.ImProperties;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class WebSocketChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ImProperties imProperties;

    @Autowired
    private HttpRequestHandler httpRequestHandler;

    @Autowired
    private WebSocketServerHandler webSocketServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        log.info(imProperties.toString());
        // 添加心跳处理器
        pipeline.addLast(new IdleStateHandler(imProperties.getIdleReadTimeout(),
                imProperties.getIdleReadTimeout(), imProperties.getIdleAllTimeout(), TimeUnit.SECONDS));
        pipeline.addLast("http-codec", new HttpServerCodec()); // HTTP编码解码器
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536)); // 把HTTP头、HTTP体拼成完整的HTTP请求
        pipeline.addLast("http-chunked", new ChunkedWriteHandler()); // 分块，方便大文件传输，不过实质上都是短的文本数据
        //增加了下面的http请求处理器，这个就失效了
//        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast("http-handler", httpRequestHandler);
        pipeline.addLast("websocket-handler",webSocketServerHandler);
    }
}
