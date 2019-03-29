package com.charles.def;

/**
 * 通用的枚举类型接口定义
 *
 * @author CharlesLee
 */
public interface GenericDefinition<K, V> {

    /**
     * 返回key
     */
    K getKey();

    /**
     * 返回v
     */
    V getValue();


    /**
     * 获取自己所有
     */
    GenericDefinition<K, V> get();
}
