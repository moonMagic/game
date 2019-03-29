package com.charles.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * corn表达式,配置循环定时任务
 *
 * @author CharlesLee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cron {

    /**
     * cron 表达式字符串,需要注意的是一定要保证表达式的正确性,否则任务无法启动
     */
    String value() default "";

    /**
     * 是否自动启动 ，默认不自动启动
     */
    boolean autoStart() default false;

    /**
     * 任务是否只启动一次,如果只启动一次的话，那么就会立即执行，
     */
    boolean forOnce() default false;
}
