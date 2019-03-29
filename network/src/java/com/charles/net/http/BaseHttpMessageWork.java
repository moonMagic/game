package com.charles.net.http;

import com.charles.net.BaseNetWork;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * http逻辑处理类,根据http的请求路径不同来处理不同的任务
 *
 * @author CharlesLee
 */
public abstract class BaseHttpMessageWork extends BaseNetWork {

    public BaseHttpMessageWork(String taskName) {
        super(taskName);
    }

    /**
     * 请求参数的详细信息
     */
    protected Map<String, String> paramMap;


    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }


    /**
     * 将处理完毕的消息回馈给客户端
     *
     * @param message : 处理完毕以后需要回馈的消息
     * @author CharlesLee
     */
    protected void response(String message) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(message.getBytes(StandardCharsets.UTF_8)));
        // 以纯文本的方式进行数据返回, 客户端收到会直接使用,不会转换
        response.headers().set(CONTENT_TYPE, "text/plain");
        // 告诉客户端该文本数据的长度
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set("Access-Control-Allow-Origin", "*");
        ctx.writeAndFlush(response);
    }
}
