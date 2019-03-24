package com.charles.def.interfaces;

/**
 * 系统定义声明,该接口中定义了系统相关常量
 *
 * @author CharlesLee
 */
public interface SystemDef {

    /**
     * 获取当前计算机CPU核心线程数
     */
    int SYSTEM_CPU_COUNT = Runtime.getRuntime().availableProcessors();
}
