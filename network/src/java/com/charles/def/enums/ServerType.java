package com.charles.def.enums;

import com.charles.def.GenericDefinition;

/**
 * 定义服务类型枚举, 现在只先定义常见类型
 *
 * @author CharlesLee
 */
public enum ServerType implements GenericDefinition<Integer, String> {

    /**
     * 网关服务器类型
     */
    GATEWAY_SERVICE(0, "网关服务器所代表的类型");


    ServerType(Integer type, String messageTypeDescribe) {
        this.type = type;
        this.messageTypeDescribe = messageTypeDescribe;
    }

    /**
     * 消息体类型描述信息
     */
    private String messageTypeDescribe;

    /**
     * 消息体类型
     */
    private Integer type;


    /**
     * 通过客户端传入的数据消息类型进行枚举类型的查找,如果没有查找到指定的类型，那么则返回null
     *
     * @author charlesLee
     */
    public static GenericDefinition get(int type) {
        for (GenericDefinition<Integer, String> value : ServerType.values()) {
            if (type == value.getKey()) {
                return value;
            }
        }
        return null;
    }


    @Override
    public Integer getKey() {
        return type;
    }

    @Override
    public String getValue() {
        return messageTypeDescribe;
    }

    @Override
    public GenericDefinition<Integer, String> get() {
        return this;
    }
}
