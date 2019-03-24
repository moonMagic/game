package com.charles.net.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http服务启动类,该服务类只允许客户端请求,其余请求都不处理
 *
 * @author CharlesLee
 */
public class HttpServer implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(HttpServer.class);

    /**
     * http请求的端口号
     */
    private int port;

    /**
     * 数据交互的通道
     */
    private ChannelFuture channelFuture;

    private NioEventLoopGroup workGroup;

    public HttpServer start(int port, NioEventLoopGroup workGroup) {
        this.port = port;
        this.workGroup = workGroup;
        Thread httpServerThread = new Thread(this);
        httpServerThread.setName("HttpServerThread -> " + port);
        httpServerThread.start();
        return this;
    }

    @Override
    public void run() {
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(workGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            socketChannel.pipeline().addLast(new HttpObjectAggregator(65535));
                            // 该通道处理器主要是为了处理大文件传输的情形。大文件传输时，需要复杂的状态管理。
                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());
//                            socketChannel.pipeline().addLast(new HttpMessageHandler());
                        }
                    });

            channelFuture = server.bind(port).sync();
            LOGGER.warn(" -- > Http service started successfully, the port number of the binding is : " + port);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.warn(" -- > Http service startup exception ", e);
        } finally {
            // 线程池优雅退出,只要端口没有被占用,那么该 finally 不会被触发
            workGroup.shutdownGracefully();
        }
    }

    /**
     * @author CharlesLee
     */
    public void closeHttpServer() {
        channelFuture.channel().close();
        LOGGER.warn(" -- > HTTP service has been successfully closed!");
    }
}
