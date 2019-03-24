package com.charles.net.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 超时处理器
 *
 * @author charlesLee
 */
public class TimeoutProcessor extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(TimeoutProcessor.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            // 触发读取超时，这个用户没有与服务器进行连接了
            if (e.state() == IdleState.READER_IDLE) {
                LOGGER.error("server read idle,disconnect user");
            } else if (e.state() == IdleState.WRITER_IDLE) {
                LOGGER.warn("server write idle..ctx:{}, evt:{}", ctx.channel(), evt);
            }
            // 数据通道链接断开
            ctx.disconnect();
        }
    }
}