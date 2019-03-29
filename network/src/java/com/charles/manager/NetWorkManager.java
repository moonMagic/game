package com.charles.manager;

import com.charles.def.interfaces.SystemDef;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 网络连接管理器
 *
 * @author CharlesLee
 */
public class NetWorkManager {

    private NetWorkManager() {

    }

    private static final NetWorkManager NET_WORK_MANAGER = new NetWorkManager();

    public static NetWorkManager getInstance() {
        return NET_WORK_MANAGER;
    }

    private static final Logger LOGGER = LogManager.getLogger(NetWorkManager.class);

    /**
     * TCP监听器端口线程组,该线程组只需要最多监听一个端口，所以只需要初始化二条监听线程即可
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup(SystemDef.SYSTEM_CPU_COUNT, new DefaultThreadFactory("netty-tcp-server-accept", true));

    /**
     * boss group 线程组监听策略, 如果是网关服务因为需要启动大量的连接,所以需要启动多条线程,而内部服务的任务接收与发送只需要一条线程即可,因为不会有大量连接申请
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup(2, new DefaultThreadFactory("netty-tcp-server-accept", true));

    /**
     * TCP网络读写监听线程组,该线程组需要监听所有的网络读写操作，但是任务线程池也需要分配一定的资源，所以这里只设置所有CPU数量的线程即可
     */
    private EventLoopGroup workerGroup =
            new NioEventLoopGroup(SystemDef.SYSTEM_CPU_COUNT, new DefaultThreadFactory("netty-tcp-server-monitoring-read-write", true));

    /**
     * http服务线程组,http服务不参与网络任务,只提供查询等后台功能,所以使用量非常少,初始化一条线程足够了
     */
    private EventLoopGroup httpWorkGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-http-server", true));


    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public EventLoopGroup getHttpWorkGroup() {
        return httpWorkGroup;
    }
}
