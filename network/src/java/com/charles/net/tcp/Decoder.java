package com.charles.net.tcp;

import com.charles.manager.RecyclableObjectFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * TCP消息解码器, 将数据通道中的数据流信息进行反向编码，自定义协议
 *
 * @author charlesLee
 */
public final class Decoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LogManager.getLogger(Decoder.class);

    /**
     * 当解析不了得数据临时存放之地
     */
    private ByteBuf bytes;

    /**
     * @param input 本次接收到的数据
     * @author charlesLee
     * 重载上一次预留字节数量
     **/
    private ByteBuf bytesRead(ByteBuf input) {
        ByteBuf byteBuf;
        if (bytes != null) {
            byteBuf = Unpooled.buffer();
            byteBuf.writeBytes(bytes);
            bytes = null;
            byteBuf.writeBytes(input);
        } else {
            byteBuf = input;
        }
        return byteBuf;
    }

    /**
     * @param input 本次读取到的数据
     * @author charlesLee
     * 留存无法读取的byte 等待下一次接收到的数据包
     **/
    private void bytesPreserve(ByteBuf input) {
        bytes = Unpooled.buffer();
        bytes.writeBytes(input);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        if (buf.readableBytes() > 0) {
            ByteBuf read = bytesRead(buf);
            messageProcessing(read, list);
            if (read.readableBytes() > 0) {
                // 将无法读取的数据预留下来
                bytesPreserve(read);
            }
        } else {
            // todo 空包, 暂不处理
            LOGGER.error("接收到了一次空包");
        }
    }

    /**
     * @param messageList 接收到的数据组装的列表
     * @param read        本次接受到的数据
     * @author charlesLee
     * 消息处理器，将数据通道中的数据经过解析变成普通可读取数据
     **/
    private void messageProcessing(ByteBuf read, List<Object> messageList) {
        while (read.readableBytes() > 0) {
            // 反向解析消息头部信息,这里需要先把消息头部数据给拿出来
            int allocationId = read.readInt();
            int serverId = read.readInt();
            int requestFrequency = read.readInt();
            int responseFrequency = read.readInt();
            byte messageType = read.readByte();
            int cmd = read.readInt();
            int messageSize = read.readInt();
            int reserve1 = read.readInt();
            int reserve2 = read.readInt();
            int statusCode = read.readInt();
            int popupType = read.readInt();
            int popupTime = read.readInt();
            // 如果是完整的一条消息
            if (read.readableBytes() >= messageSize) {
                // 完整的消息
                byte[] byteBuffer = new byte[messageSize];
                // 读取有效数据
                read.getBytes(read.readerIndex(), byteBuffer);
                Message message = RecyclableObjectFactory.getInstance().get(Message.class);
                message.setAllocationId(allocationId);
                message.setServerId(serverId);
                message.setRequestFrequency(requestFrequency);
                message.setResponseFrequency(responseFrequency);
                message.setMessageType(messageType);
                message.setCmd(cmd);
                message.setReserve1(reserve1);
                message.setReserve2(reserve2);
                message.setStatusCode(statusCode);
                message.setPopupType(popupType);
                message.setPopupTime(popupTime);
                message.setMessage(byteBuffer);
                message.setMessageSize(messageSize);
                messageList.add(message);
                // 读取消息的下标移动到指定位置
                read.readerIndex(read.readerIndex() + messageSize);
            } else {
                // 数据读取失败，字节下标归位，归还的数据为自定义tcp协议头部数据信息
                read.readerIndex(read.readerIndex() - Message.TCP_HAND_SIZE);
                break;
            }
        }
    }
}
