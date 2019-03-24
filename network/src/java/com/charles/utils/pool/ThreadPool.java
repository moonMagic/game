package com.charles.utils.pool;

import com.charles.def.interfaces.SystemDef;
import com.charles.utils.thread.BaseTaskRunnable;
import com.charles.utils.thread.BaseTaskThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;

/**
 * 线程和线程池管理类,用来管理多条底层线程和任务处理逻辑线程池
 *
 * @author CharlesLee
 */
public final class ThreadPool {

    private static final Logger LOGGER = LogManager.getLogger(ThreadPool.class);

    private static final ThreadPool THREAD_POOL = new ThreadPool();

    private ThreadPool() {

    }

    public static ThreadPool getInstance() {
        return THREAD_POOL;
    }

    /**
     * 通过 ThreadPoolExecutor 创建线程池, 以便于更好的掌控线程池,更能有效避免系统资源耗尽等异常情况
     * SystemDef.SYSTEM_CPU_COUNT 常量获取当前系统CPU核心线程数量
     * <p>
     * corePoolSize 核心线程数量,会一直存活的线程,即便是没有任务, 当前设置为系统CPU核心数量的一半
     * maximumPoolSize 线程池维护线程的最大数量, 无论任务有多少,都不会超过这个数量, 当前设置为系统CPU核心数量的四倍
     * keepAliveTime  线程池维护线程所允许的空闲时间，当线程空闲时间达到这个时间，该线程会退出，直到线程数量等于corePoolSize, 当前设置为20
     * unit 线程池维护线程所允许的空闲时间的单位, 这里设置为小时
     * workQueue 线程池所使用的缓冲队列
     * handler: 线程池中的数量大于maximumPoolSize，对拒绝任务的处理策略，默认值ThreadPoolExecutor.AbortPolicy()。这里使用ThreadPoolExecutor.CallerRunsPolicy(),会再次重试该任务
     */
    private MyThreadPoolExecutor pool = new MyThreadPoolExecutor(SystemDef.SYSTEM_CPU_COUNT
            , SystemDef.SYSTEM_CPU_COUNT * 2
            , 0L
            , TimeUnit.MILLISECONDS
            , new LinkedBlockingQueue<>()
            , new ThreadPoolExecutor.CallerRunsPolicy());


    /**
     * 将一个没有返回值的任务提交至线程池
     *
     * @author CharlesLee
     */
    public final void assignTask(Runnable work) {
        checkThread(work);
        pool.execute(work);
    }


    private void checkThread(Runnable work) {
        if (!(work instanceof BaseTaskRunnable || work instanceof BaseTaskThread)) {
            throw new RuntimeException("任务线程： " + work.getClass().getName() + "非项目内部标准线程，请使用 {com.charles.utils.thread.BaseTaskRunnable} 或者 {com.charles.utils.thread.BaseTaskThread} 替代");
        }
    }


    /**
     * 获取任务队列的大小
     */
    public int workSize() {
        return pool.getQueue().size();
    }

    /**
     * 线程死锁检测
     * 检测代码中发生的死锁
     *
     * @author CharlesLee
     */
    public void deadlockDetection() {
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        // 查找处于死锁状态的线程周期
        long[] ids = tmx.findDeadlockedThreads();
        if (ids != null) {
            StringBuilder builder = new StringBuilder();
            ThreadInfo[] infos = tmx.getThreadInfo(ids, true, true);
            if (infos != null && infos.length > 0) {
                for (ThreadInfo ti : infos) {
                    builder.append(ti.toString());
                }
                LOGGER.error("下列线程发生了死锁 : \n" + builder.toString());
                // todo 发生了死锁？怎么办！
            }
        }
    }

    public MyThreadPoolExecutor getPool() {
        return pool;
    }
}
