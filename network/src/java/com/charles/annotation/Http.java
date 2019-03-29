package com.charles.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http请求处理,根据不同的请求路径进行不同的处理
 *
 * @author CharlesLee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Http {

    /**
     * 请求路径,处理器需要根据此字段进行查找
     *
     * @author CharlesLee
     */
    String requestUrl();
}
