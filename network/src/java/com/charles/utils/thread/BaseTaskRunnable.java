package com.charles.utils.thread;

/**
 * 自定义任务线程
 * 需要注意的是该线程是提交至线程池的，如果不是提交至线程池的任务不是必须继承该类
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
        completionLogicalTask(System.currentTimeMillis());
        record();
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
