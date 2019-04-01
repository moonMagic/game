package com.charles.net.tcp;


import com.charles.config.SystemConfig;
import com.charles.utils.StringUtil;
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

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * tcp客户端启动类
 *
 * @author charlesLee
 */
public class TcpClient {

    private static final Logger LOGGER = LogManager.getLogger(TcpClient.class);

    private  SystemConfig systemConfig;

    private Map<String, Channel> clientCache = new ConcurrentHashMap<>();

    /**
     * 为该客户端监听启动的线程的名字
     */
    private String threadName;

    private Bootstrap clientBootStrap;

    private NioEventLoopGroup group;


    /**
     * tcp客户端启动方法，这里单独启动一条线程进行客户端的启动
     *
     * @author charlesLee
     */
    public void start(String threadName, NioEventLoopGroup group) {
        this.threadName = threadName;
        this.group = group;
    }


    private void initBootstrap() {
        clientBootStrap = new Bootstrap();
        clientBootStrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
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
//                        p.addLast(new handler());
                    }
                });
    }


    public void bind(String serverAddress, int port) {
        if (serverAddress == null || "".equals(serverAddress) || port == 0) {
            throw new IllegalArgumentException("需要绑定的地址和端口都不可以为空...");
        }
        String key = StringUtil.addressPortCombinationRules(serverAddress, port);
        Channel channel = clientCache.get(key);
        if (channel != null) {
            throw new RuntimeException(key + ", 不可连接通道...");
        }
        ChannelFuture future = clientBootStrap.bind(new InetSocketAddress(serverAddress, port));
        clientCache.put(key, future.channel());
    }


    /**
     * 客户端向服务端申请注册
     *
     * @author charlesLee
     */
    private void register(ChannelFuture clientFuture) {

    }


    /**
     * 这里代表客户端与服务端的数据通道已经成功打通
     */
    public void setCtx(ChannelHandlerContext ctx) {

    }

    public String getThreadName() {
        return threadName;
    }
}
