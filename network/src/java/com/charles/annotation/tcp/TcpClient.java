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
public @interface TcpClient {
    /**
     * 根据CMD来区分次类的功能
     *
     * @return int 需要的参数cmd消息号
     * @author CharlesLee
     */
    int cmd();
}
