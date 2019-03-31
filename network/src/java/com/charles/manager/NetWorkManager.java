package com.charles.manager;

import com.charles.config.SystemConfig;
import com.charles.def.enums.ServerType;
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

    private SystemConfig systemConfig;

    private static final NetWorkManager NET_WORK_MANAGER = new NetWorkManager();

    public static NetWorkManager getInstance() {
        return NET_WORK_MANAGER;
    }

    private static final Logger LOGGER = LogManager.getLogger(NetWorkManager.class);

    /**
     * tcp 服务端端口线程组监听
     */
    private EventLoopGroup serviceBossGroup;

    /**
     * tcp 网络读写线程组监听,服务端和客户端共用线程组
     */
    private EventLoopGroup workerGroup;

    /**
     * http服务线程组,只需要初始化一条线程,用来做端口和读写监听,因为HTTP服务有些服务端是不需要启动的
     */
    private EventLoopGroup httpWorkerAndBossGroup;

    private NetWorkManager() {
        systemConfig = ProjectManager.getInstance().getContext().getBean(SystemConfig.class);
        if (systemConfig.isServiceHttpOpen()) {
            httpWorkerAndBossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-http-server", true));
        }

        // io线程组监听策略都相同
        workerGroup =
                new NioEventLoopGroup(SystemDef.SYSTEM_CPU_COUNT < 4 ? SystemDef.SYSTEM_CPU_COUNT * 2 : SystemDef.SYSTEM_CPU_COUNT,
                        new DefaultThreadFactory("netty-tcp-monitoring-read-write", true));

        int cpuSize = systemConfig.getServerType() == ServerType.GATEWAY_SERVICE.getKey() ? SystemDef.SYSTEM_CPU_COUNT / 2 : 1;
        serviceBossGroup =
                new NioEventLoopGroup(cpuSize, new DefaultThreadFactory("netty-tcp-server-accept", true));
    }



    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public EventLoopGroup getHttpWorkerAndBossGroup() {
        if (httpWorkerAndBossGroup == null || !systemConfig.isServiceHttpOpen()) {
            throw new RuntimeException("http工作线程组初始化失败或者未在配置文件中开启http服务的初始化...");
        }
        return httpWorkerAndBossGroup;
    }
}
