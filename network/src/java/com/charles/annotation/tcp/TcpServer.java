package com.charles.annotation.tcp;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TCP注解区分, 添加了次注解的将会被划分为TCP
 *
 * @author CharlesLee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TcpServer {
    /**
     * 根据CMD来区分次类的功能
     *
     * @return int 需要的参数cmd消息号
     * @author CharlesLee
     */
    int cmd();

    /**
     * 指定端口号专用服务,非该端口不能使用,默认为0通用端口,只有当专用端口获取不到的情况下才会用通用端口
     * 还需要注意的是,客户端向服务器申请建立连接后,获取到的端口应该是服务器的端口,所以
     */
    int port() default 0;
}
