package com.charles.net;

import com.charles.manager.RecyclableObjectFactory;
import com.charles.net.tcp.Message;
import com.charles.utils.thread.BaseTaskRunnable;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author CharlesLee
 */
public abstract class BaseNetWork extends BaseTaskRunnable {

    public BaseNetWork(String taskName) {
        super(taskName);
    }

    protected ChannelHandlerContext ctx;

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

}
