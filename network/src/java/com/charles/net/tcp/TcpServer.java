package com.charles.net.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * tcp服务启动类
 *
 * @author charlesLee
 */
public final class TcpServer implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(TcpServer.class);

    /**
     * 指定绑定的端口号
     **/
    private int port;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 通道管理器,数据交互的口子
     */
    private ChannelFuture serverChannel;
    /**
     * Acceptor 线程池,负责处理客户端的 TCP 连接请求
     */
    private EventLoopGroup bossGroup;
    /**
     * 负责 I/O 读写操作的线程组,通过 ServerBootstrap 的 group 方法进行设置,用于后续的 Channel 绑定。
     */
    private EventLoopGroup workerGroup;

    private String serverName;

    public TcpServer(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 启动服务器
     * 这里的服务只是单独来启动
     */
    public TcpServer start(int port, NioEventLoopGroup bossGroup, NioEventLoopGroup workerGroup) {
        this.port = port;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        Thread thread = new Thread(this);
        thread.setName(serverName == null ? "tcpServerThread -> " + port : serverName + " -> " + port);
        thread.start();
        return this;
    }

    @Override
    public void run() {

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("timeOut", new TimeoutProcessor());
                            ch.pipeline().addLast(new Decoder());
                            ch.pipeline().addLast(new Encoder());
                            ch.pipeline().addLast(new MessageHandler());
                        }
                    }).option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_REUSEADDR, true);
            serverChannel = b.bind(port).sync();
            LOGGER.warn(" -- > TCP service startup completion, Successful binding port number is " + port);
            serverChannel.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(" -- > TCP service startup failure! ", e);
        } finally {
            // 优雅退出
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * close
     */
    public void closeTcpServer() {
        serverChannel.channel().close();
        LOGGER.warn(" -- > TCP service has been successfully closed!");
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getPort() {

        return port;
    }
}
