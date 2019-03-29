package com.charles.net.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * http消息处理器,用于读取http请求消息并处理
 * <p>
 * 需要说明的是http的消息可能非常少,所以并不用去考虑效率的问题
 *
 * @author CharlesLee
 */
public class HttpMessageHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(HttpMessageHandler.class);

    /**
     * 消息处理方法, 解析http请求数据, 并调用相应的处理类进行处理
     *
     * @param ctx : 数据处理通道
     * @param msg : 需要处理的数据
     * @author CharlesLee
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // 如果是请求
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            Map<String, String> map = new HashMap<>();
            // 如果是post请求
            if (request.method() == HttpMethod.POST) {
                analysisRequestByPost(request, msg, map);
                // 如果是get请求
            } else if (request.method() == HttpMethod.GET) {
                analysisRequestByGet(request, map);
            } else {
                // 如果不是get或者post请求, 直接跳过该请求
                return;
            }
            // 将ip添加至数据缓存中
            map.put("ip", getRequestIp(ctx));
            String uri = request.uri();
            String path = null;
            if (!Objects.isNull(uri) && !"".equals(uri)) {
                // 将请求路径和所带的参数分离出来,如果能拆分那么前面的数据就是路径,后面的数据就是参数组合,如果无法拆分,说明只有路径
                String[] kv = uri.split("\\?");
                path = kv[0];
            }

//            // 首先根据解析出来的请求路径找到对应的处理任务
//            BaseHttpMessageWork work = ApplicationContext.getInstance().getBeanSingle(HttpManager.class).get(path);
//            if (work == null) {
//                // 错误的请求则不作任何处理
//                LOGGER.warn("this " + path + " request is empty");
//            } else {
//                work.setCtx(ctx);
//                work.setParamMap(map);
//                ThreadPool.getInstance().assignTask(work);
//            }
        }
    }

    /**
     * 获取请求ip地址
     *
     * @param context : 数据通道
     * @return java.lang.String
     * @author CharlesLee
     */
    private String getRequestIp(ChannelHandlerContext context) {
        if (context.channel().remoteAddress() != null) {
            InetSocketAddress address = (InetSocketAddress) context.channel().remoteAddress();
            if (address != null) {
                if (address.getAddress() != null) {
                    return address.getAddress().getHostAddress();
                }
            }
        }
        return null;
    }

    /**
     * 解析get请求数据
     *
     * @param request : 请求对象
     * @author CharlesLee
     */
    private void analysisRequestByGet(FullHttpRequest request, Map<String, String> map) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> params = queryStringDecoder.parameters();
        if (!params.isEmpty()) {
            for (Map.Entry<String, List<String>> p : params.entrySet()) {
                String key = p.getKey();
                List<String> list = p.getValue();
                for (String val : list) {
                    map.put(key, val);
                }
            }
        }
    }


    /**
     * 解析http请求中的post请求数据
     *
     * @param request : 请求对象
     * @param msg     : 数据
     * @author CharlesLee
     */
    private void analysisRequestByPost(FullHttpRequest request, Object msg, Map<String, String> map) {
        // 创建一个map集合来保存解析的数据
        HttpHeaders headers = request.headers();
        String dataType = headers.get("content-type");
        // 如果为表单数据请求
        if (dataType.contains("form_date")) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            HttpContent chunk = (HttpContent) msg;
            decoder.offer(chunk);
            List<InterfaceHttpData> list = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : list) {
                Attribute param = (Attribute) data;
                try {
                    map.put(param.getName(), param.getValue());
                } catch (IOException e) {
                    LOGGER.error("Parsing POST request data error", e);
                }
            }
        } else {
            // 如果没有找到表单数据,那么尝试使用字符串拆分的方式进行数据解析
            int len = request.content().readableBytes();
            if (len > 0) {
                byte[] content = new byte[len];
                request.content().readBytes(content);
                String string = new String(content, StandardCharsets.UTF_8);
                stringSplitPostMessage(map, string);
            }
        }
    }

    /**
     * 尝试使用字符串拆分的方式进行post请求数据的解析
     *
     * @param map     : 需要将拆分的消息装入的集合
     * @param message : 需要拆分的消息
     */
    private void stringSplitPostMessage(Map<String, String> map, String message) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (message != null && !"".equals(message)) {
            String[] requestMessageKeyAndValue = message.split("&");
            if (requestMessageKeyAndValue.length > 0) {
                for (String keyAndValue : requestMessageKeyAndValue) {
                    String[] result = keyAndValue.split("=");
                    if (result.length == 2) {
                        map.put(result[0], result[1]);
                    }
                }
            }
        }
    }
}
