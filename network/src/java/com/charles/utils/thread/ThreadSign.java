package com.charles.utils.thread;

/**
 * 线程标记
 *
 * @author CharlesLee
 */
public interface ThreadSign {


    /**
     * 启动业务逻辑
     */
    void startBusinessLogic();

    /**
     * 当完成逻辑任务后执行
     */
    void completionLogicalTask();
}
