package com.charles.lee.cron.servers.base;

import com.charles.lee.cron.def.interfaces.StringDef;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 任务接口的父类,所有的任务都必须继承至该类
 * 因为任务涉及到初始化服务与自动运行,如果不继承该抽象类那么任务则无法自动运行
 *
 * @author CharlesLee
 */
public abstract class BaseTimerWork implements Job {

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        Object[] args = (Object[]) arg0.getJobDetail().getJobDataMap().get(StringDef.ARGS);
        init(args);
        try {
            go();
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    /**
     * 初始化参数
     */
    public abstract void init(Object... args);

    /**
     * 任务运行
     */
    public abstract void go() throws Exception;
}
