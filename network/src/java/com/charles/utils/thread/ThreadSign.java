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
     *
     * @param workEndTimer 任务执行完毕的时间
     */
    void completionLogicalTask(long workEndTimer);

    /**
     * 进行日志的记录处理
     */
    void record();
}
