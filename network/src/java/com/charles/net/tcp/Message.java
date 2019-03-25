package com.charles.net.tcp;

import com.charles.entity.BaseRecyclable;

/**
 * 消息体对象，用来接收和发送消息，消息头长度计算规则：
 * byte     1字节
 * short    2字节
 * int      4字节
 * long     8字节
 * char     2字节
 * float    4字节
 * double   8字节
 *
 * @author CharlesLee
 */
public class Message extends BaseRecyclable {

    /**
     * TCP头部消息长度,
     */
    static final int TCP_HAND_SIZE = 4 + 4 + 4 + 4 + 1 + 4 + 4 + 4 + 4 + 4 + 4 + 4;

    /**
     * 用户id分配编号，没有则为0
     */
    private int allocationId;

    /**
     * 服务器ID编号
     */
    private int serverId;

    /**
     * 客户端主动请求次数，心跳消息不计算，客户端请求第一次默认为1
     */
    private int requestFrequency;

    /**
     * 服务器回复次数，服务器的第一次回复默认为1;
     */
    private int responseFrequency;

    /**
     * 数据类型
     */
    private byte messageType;

    /**
     * 命令号
     */
    private int cmd;

    /**
     * 消息体长度
     */
    private int messageSize;

    /**
     * 预留字段1
     */
    private int reserve1;

    /**
     * 预留字段2
     */
    private int reserve2;


    /**
     * 状态码，也叫做错误码，根据该码就可以获取到回馈的错误类型
     */
    private int statusCode;

    /**
     * 弹窗类型
     */
    private int popupType;

    /**
     * 弹窗时长, 当弹窗类型不为默认值的时候触发
     */
    private int popupTime;


    /**
     * 消息体具体内容
     */
    private byte[] message;


    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getRequestFrequency() {
        return requestFrequency;
    }

    public void setRequestFrequency(int requestFrequency) {
        this.requestFrequency = requestFrequency;
    }

    public int getResponseFrequency() {
        return responseFrequency;
    }

    public void setResponseFrequency(int responseFrequency) {
        this.responseFrequency = responseFrequency;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public int getReserve1() {
        return reserve1;
    }

    public void setReserve1(int reserve1) {
        this.reserve1 = reserve1;
    }

    public int getReserve2() {
        return reserve2;
    }

    public void setReserve2(int reserve2) {
        this.reserve2 = reserve2;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public int getPopupType() {
        return popupType;
    }

    public void setPopupType(int popupType) {
        this.popupType = popupType;
    }

    public int getPopupTime() {
        return popupTime;
    }

    public void setPopupTime(int popupTime) {
        this.popupTime = popupTime;
    }

    @Override
    protected void recycle() {
        this.allocationId = 0;
        this.serverId = 0;
        this.requestFrequency = 0;
        this.responseFrequency = 0;
        this.messageType = 0;
        this.cmd = 0;
        this.messageSize = 0;
        this.reserve1 = 0;
        this.reserve2 = 0;
        this.statusCode = 0;
        this.popupType = 0;
        this.popupTime = 0;
        this.message = null;
    }
}
