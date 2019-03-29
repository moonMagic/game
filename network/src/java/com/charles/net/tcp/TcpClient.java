package com.charles.net.tcp;


import com.charles.config.SystemConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.util.concurrent.TimeUnit;

/**
 * tcp客户端启动类
 *
 * @author charlesLee
 */
@Component
public class TcpClient implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(TcpClient.class);

    private final SystemConfig systemConfig;

    /**
     * 为该客户端监听启动的线程的名字
     */
    protected String threadName;

    @Autowired
    public TcpClient(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }


    /**
     * tcp客户端启动方法，这里单独启动一条线程进行客户端的启动
     *
     * @author charlesLee
     */
    public TcpClient start(String threadName) {
        this.threadName = threadName;
        Thread thread = new Thread(this);
        thread.setName((threadName == null || "".equals(threadName)) ? "tcp-client-listener-service-thread" : threadName);
        thread.start();
        return this;
    }

    @Override
    public void run() {
        connect();
    }

    /**
     * @author charlesLee
     * 启动客户端用来绑定服务端的端口
     **/
    private void connect() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(0, 0, 180, TimeUnit.SECONDS));
                            p.addLast(new Decoder());
                            p.addLast(new Encoder());

                        }
                    });
            // 绑定
            ChannelFuture clientFuture = b.connect(serverAddress, serverPort).sync();
            // 客户端向服务器申请注册
            register(clientFuture);
            clientFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            connectionDisconnect = true;
            // 这里编译器警告忽略，如果socket链接异常的话，这里一定会触发的
            if (e instanceof SocketException) {
                LOGGER.error(getDef() + ",链接异常，正在重复尝试中......");
            } else {
                LOGGER.error(getDef() + ",未知连接错误.......");
            }
        } finally {
            // 当与服务器断开连接之后，这里会调用
            connectionDisconnect = true;
            group.shutdownGracefully();
            // 五秒一次，自动重连
            try {
                Thread.sleep(5000);
                connect();
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * @author charlesLee
     * 客户端向服务端申请注册
     */
    private void register(ChannelFuture clientFuture) {

    }


    /**
     * 这里代表客户端与服务端的数据通道已经成功打通
     */
    public void setCtx(ChannelHandlerContext ctx) {

    }
}
