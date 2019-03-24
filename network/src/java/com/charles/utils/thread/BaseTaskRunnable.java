package com.charles.utils.thread;

/**
 * 自定义任务线程
 *
 * @author CharlesLee
 */
public abstract class BaseTaskRunnable implements Runnable, ThreadSign {

    private BaseTaskRunnable() {
    }

    public BaseTaskRunnable(String taskName) {
        this.taskName = taskName;
        this.createTime = System.currentTimeMillis();
    }

    /**
     * 设置的任务名称
     */
    private String taskName;

    /**
     * 任务启动的时间，毫秒数
     */
    private volatile long startTime;


    /**
     * 该任务创建的时间，毫秒数
     */
    private volatile long createTime;

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        startBusinessLogic();
        long endTime = System.currentTimeMillis();
        completionLogicalTask();
    }

    public String getTaskName() {
        return taskName;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getCreateTime() {
        return createTime;
    }
}
