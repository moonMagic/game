package com.charles.utils.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

/**
 * @author CharlesLee
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger LOGGER = LogManager.getLogger(MyThreadPoolExecutor.class);

    /**
     * 任务是否需要暂停，默认是不需要暂停的，任务暂停中任务还是可以进行提交
     * true，任务暂停中...
     * false，任务进行中...
     */
    private boolean pause = false;

    /**
     * 任务暂停的开始时间,当任务在启动中的时候,改值为0
     */
    private long pauseStartTime;

    /**
     * 临时任务保存区
     */
    private final BlockingQueue<Runnable> temporaryTask = new LinkedBlockingQueue<>();

    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    /**
     * 这里扩展一下线程池的功能
     */
    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new RuntimeException();
        }
        if (pause) {
            temporaryTask.add(command);
        } else {
            super.execute(command);
        }
    }

    public boolean isPause() {
        return pause;
    }

    /**
     * 线程池状态改变所需要做的处理
     */
    private void setPause(boolean pause) {
        if (pause == this.pause) {
            return;
        }
        this.pause = pause;
        // 如果是需要暂停任务
        if (pause) {
            temporaryTask.clear();
            temporaryTask.addAll(getQueue());
            getQueue().clear();

        } else {
            //如果是需要继续进行任务
            for (; ; ) {
                Runnable runnable = temporaryTask.poll();
                if (runnable != null) {
                    execute(runnable);
                } else {
                    return;
                }
            }
        }
    }

    /**
     * 调用该方法使线程池进入暂停状态
     */
    public void getIntoPauseTime() throws Exception {
        // 如果已经是任务暂停状态,那么
        if (pause) {
            try {
                throw new RuntimeException("警告,线程池已经进入暂停状态,调用错误,进入暂停的时间为: " + pauseStartTime);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e);
                throw e;
            }
        } else {
            pause = true;
            pauseStartTime = System.currentTimeMillis();
            setPause(true);
        }
    }

    /**
     * 调用该方法使线程池从暂停状态改变为启动状态
     */
    public void start() throws Exception {
        if (!pause) {
            try {
                throw new RuntimeException("警告,线程池未进入暂停状态,调用错误,已经暂停的时间长度为: " + (System.currentTimeMillis() - pauseStartTime));
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e);
                throw e;
            }
        } else {
            pause = false;
            pauseStartTime = 0;
            setPause(false);
        }
    }
}
