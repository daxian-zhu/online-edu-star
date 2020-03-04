package com.clark.daxian.im.netty;

import com.clark.daxian.im.netty.handler.WebSocketChildChannelHandler;
import com.clark.daxian.im.properties.ImProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * netty服务器配置
 * @author 大仙
 */
@Slf4j
@Component
public class NettyServer {

    /**
     * 创建bootstrap 服务启动引导类
     */
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
    /**
     * 监听端口，accept 新连接的线程组
     */
    private final EventLoopGroup boss = new NioEventLoopGroup();
    /**
     * Worker 处理每一条连接的数据读写的线程组
     */
    private final EventLoopGroup work = new NioEventLoopGroup();
    /**
     * NETT服务器配置类
     */
    @Autowired
    private  ImProperties imProperties;

    @Autowired
    private WebSocketChildChannelHandler webSocketChildChannelHandler;

    private ChannelFuture serverChannelFuture;

    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        log.info("关闭服务器....");
        //优雅退出
        closeNetty();
    }
    /**
     * 开启及服务线程
     */
    public void start() {
        // 从配置文件中获取服务端监听端口号
        Integer port = imProperties.getPort();
        //设置2大线程组
        serverBootstrap.group(boss, work)
                //指定IO模型 这里是NIO  OioServerSocketChannel为BIO
                .channel(NioServerSocketChannel.class)
                //用于临时存放已完成三次握手的请求的队列的最大长度
                .option(ChannelOption.SO_BACKLOG, 100);
        /**
         * 服务端启动过程中的一些逻辑
         */
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
            @Override
            protected void initChannel(NioServerSocketChannel ch) {
                log.info("服务启动中");
            }
        });
        //设置每个连接的事件处理
        serverBootstrap.childHandler(webSocketChildChannelHandler);
        //设置每个连接的属性
        serverBootstrap
                //开启TCP底层心跳机制，true为开启
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 关闭Nagle算法，true表示关闭，false表示开启，
                // 通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
                .childOption(ChannelOption.TCP_NODELAY, true);
        log.info("netty服务器在[{}]端口启动监听", port);
        try {
            //绑定
            serverChannelFuture = serverBootstrap.bind(port).sync();
            //等待服务器关闭
            //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
            serverChannelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            closeNetty();
        }
    }
    /**
     * 描述：关闭Netty Websocket服务器，主要是释放连接
     *     连接包括：服务器连接serverChannel，
     *     客户端TCP处理连接bossGroup，
     *     客户端I/O操作连接workerGroup
     *
     *     若只使用
     *         bossGroupFuture = bossGroup.shutdownGracefully();
     *         workerGroupFuture = workerGroup.shutdownGracefully();
     *     会造成内存泄漏。
     */
    public void closeNetty(){
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = boss.shutdownGracefully();
        Future<?> workerGroupFuture = work.shutdownGracefully();

        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
    }

}
