package com.charles.utils.thread;

/**
 * @author CharlesLee
 */
public abstract class BaseTaskThread extends Thread {

    private BaseTaskThread() {
    }

    /**
     * 任务线程设置替换策略，避免不符合规范的线程进入运行
     */
    public BaseTaskThread(String taskName, BaseTaskRunnable runnable) {
        super(runnable);
        this.setName(taskName);
    }
}
