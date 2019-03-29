package com.charles.net.tcp;

import com.charles.manager.RecyclableObjectFactory;
import com.charles.net.BaseNetWork;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author CharlesLee
 */
public abstract class BaseTcpMessageWork extends BaseNetWork {

    public BaseTcpMessageWork(String taskName) {
        super(taskName);
    }

    protected ChannelHandlerContext ctx;

    protected Message message;

    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * 该任务是否给发送方做了回复,只有内部服务,才会触发的自动回复
     * <p>
     * true:  该任务已经做了回复了,不需要进行自动回复了
     * false: 该任务没有回复,需要回复发送方,这边的任务已经处理完毕了
     */
    protected boolean messageReplyStatus;


    /**
     * 需要回复的消息
     */
    protected void response(Message res) {
        ctx.writeAndFlush(res);
        messageReplyStatus = true;
    }

    /**
     * 自动回复
     */
    protected final void automaticRecovery() {
        if (!messageReplyStatus) {
            Message res = RecyclableObjectFactory.getInstance().get(Message.class);
            // todo 设计未完成...
            ctx.writeAndFlush(res);
        }
    }
}
