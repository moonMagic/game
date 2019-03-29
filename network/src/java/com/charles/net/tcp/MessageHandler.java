package com.charles.net.tcp;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author charlesLee
 */
public class MessageHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger LOGGER = LogManager.getLogger(MessageHandler.class);

    /**
     * 收到消息以后触发
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
//        if (msg.getMessageType() == MessageType.HEARTBEAT.getType()) {
//            return;
//        }
//        // 处理业务逻辑信息
//        BaseTcpMessageWork work;
//        try {
//            work = get(ctx, msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOGGER.error("查找tcp处理器过程中出现异常，请检查！：" + msg.toStringNoInfo());
//            return;
//        }
//        if (work != null) {
//            work.setMessage(msg);
//            work.setCtx(ctx);
//            ThreadPool.getInstance().assignTask(work);
//        } else {
//            LOGGER.error("未找到指定的处理器：" + msg.getCmd());
//        }
    }

    /**
     * 发现异常后触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发现了异常,异常为： " + cause);
    }

    /**
     * 断开连接后触发
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接被断开");
    }

    /**
     * 创建链路,连接被激活以后触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("链路被激活");
    }
}
