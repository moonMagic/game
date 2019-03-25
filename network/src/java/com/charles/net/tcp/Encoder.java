package com.charles.net.tcp;

import com.charles.manager.RecyclableObjectFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * tcp消息通用编码器，将需要发送的消息按照自定义规则进行组装
 *
 * @author charlesLee
 */
public final class Encoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf)  {
        // 按照顺序将协议头部数据写入，读取的时候需要按照指定的顺序进行读取
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(message.getAllocationId());
        buf.writeInt(message.getServerId());
        buf.writeInt(message.getRequestFrequency());
        buf.writeInt(message.getResponseFrequency());
        buf.writeByte(message.getMessageType());
        buf.writeInt(message.getCmd());
        buf.writeInt(message.getMessageSize());
        buf.writeInt(message.getReserve1());
        buf.writeInt(message.getReserve2());
        buf.writeInt(message.getStatusCode());
        buf.writeInt(message.getPopupType());
        buf.writeInt(message.getPopupTime());
        buf.writeBytes(message.getMessage());
        byteBuf.writeBytes(buf);
        // 对象回收,对象的内部数据已经被刷写至通道了，对象里面的数据也就没有保存的必要了
        RecyclableObjectFactory.getInstance().recycle(message);
    }
}
