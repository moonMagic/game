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

    protected Message message;

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * 发送消息, 需要注意的是成员message对象与需要发送的message对象并不是同一个对象
     *
     * @param message 将需要发送的消息推送至通道
     */
    protected final void sendMessage(Message message) {
        ctx.writeAndFlush(message);
        RecyclableObjectFactory.getInstance().recycle(message);
    }
}
