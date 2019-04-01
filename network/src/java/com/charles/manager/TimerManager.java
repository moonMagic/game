package com.charles.lee.cron.servers.manager;


import com.charles.lee.cron.annotation.Cron;
import com.charles.lee.cron.annotation.MyBean;
import com.charles.lee.cron.context.ApplicationContext;
import com.charles.lee.cron.def.interfaces.StringDef;
import com.charles.lee.cron.servers.base.BaseTimerWork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 定时任务管理器
 *
 * @author CharlesLee
 */
@MyBean
public class TimerManager {
    private static final Logger LOGGER = LogManager.getLogger(TimerManager.class);
    /**
     * 任务缓存,根据唯一任务的key和对应的任务执行编号
     * 任务的唯一key可以默认由用户自定义指定,但是对于系统来说,则默认为类加载路径
     */
    private Map<String, Long> jobsCache = new ConcurrentHashMap<>();

    private Scheduler scheduler;

    private AtomicLong identification = new AtomicLong();

    private boolean status;

    /**
     * 自动启动定时器任务
     */
    public void startTimers() throws Exception {
        if (status) {
            throw new RuntimeException("定时器任务重复启动！");
        }
        status = true;
        identification.set(System.currentTimeMillis());
        SchedulerFactory sf = new StdSchedulerFactory();
        this.scheduler = sf.getScheduler();
        this.scheduler.start();

        List<Object> beans = ApplicationContext.getInstance().getAllBean();
        for (Object obj : beans) {
            if (obj instanceof BaseTimerWork) {
                if (jobsCache.get(obj.getClass().getName()) != null) {
                    throw new RuntimeException("任务 ： " + obj.getClass().getName() + "重复启动");
                }
                Cron cron = obj.getClass().getAnnotation(Cron.class);
                if (cron == null) {
                    throw new RuntimeException("timer job has no Cron annotation");
                }
                boolean forOnce = cron.forOnce();
                if (forOnce) {
                    ((BaseTimerWork) obj).go();
                }
                boolean auto = cron.autoStart();
                if (auto) {
                    jobsCache.put(obj.getClass().getName(), submit((Class<? extends BaseTimerWork>) obj.getClass()));
                }
            }
        }
        LOGGER.info("定时器任务启动完毕！");
    }

    /**
     * 获取系统自动运行的任务的ID
     *
     * @param clazz 需要查询的指定的class
     * @return long
     * @author CharlesLee
     */
    public long getJobsCache(Class<? extends BaseTimerWork> clazz) {
        Long result = jobsCache.get(clazz.getName());
        return result == null ? 0 : result;
    }

    /**
     * 手动停止一个定时任务,该任务的停止方式为直接删除任务以及想关联的触发器
     *
     * @throws SchedulerException 停止任务时候出现异常,该异常可能包含对另外一个异常的引用，这是调度异常的根本原因
     */
    public void stopTimeWork(long id) throws SchedulerException {
        JobKey key = new JobKey(StringDef.TIMER_JOB + id);
        scheduler.deleteJob(key);
    }

    /**
     * 提交一个任务,该任务必须有cron注解
     */
    public long submitWorkByCron(BaseTimerWork timeWork, Object... args) throws SchedulerException {
        return submit(timeWork.getClass(), args);
    }

    /**
     * 提交一个定时任务,该任务的提交方式为class
     *
     * @param args  任务需要初始化的参数,有些任务需要初始化参数，而有的任务却不需要,所以采用边长任意类型参数接受
     * @param clazz 提交的任务,该任务一定是一个继承BaseTimerWork并且实现了Cron注解的类
     * @return long
     * @author CharlesLee
     */
    public long submit(Class<? extends BaseTimerWork> clazz, Object... args) throws SchedulerException {
        Cron cron = clazz.getAnnotation(Cron.class);
        if (cron == null) {
            throw new RuntimeException("timer job has no Cron annotation");
        }
        String exp = cron.value();
        if ("".equals(exp) || exp.length() == 0) {
            throw new RuntimeException("timer job Cron expression is null");
        }
        long id = identification.incrementAndGet();
        JobDetail job = JobBuilder.newJob(clazz).withIdentity(StringDef.TIMER_JOB + id).build();
        job.getJobDataMap().put(StringDef.ARGS, args);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(StringDef.TRIGGER + id).startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(exp)).build();
        scheduler.scheduleJob(job, trigger);
        return id;
    }


    /**
     * 提交延时任务,该任务不会添加至服务缓存，需要客户端自己管理
     *
     * @param clazz 初始化的任务
     * @param delay 延迟时间的毫秒数
     * @param args  需要交给任务的初始化数据
     * @return id 任务id
     */
    public long submitDelayWork(Class<? extends BaseTimerWork> clazz, long delay, Object... args) throws SchedulerException {
        long id = identification.incrementAndGet();
        JobDetail job = JobBuilder.newJob(clazz).withIdentity(StringDef.TIMER_JOB + id).build();
        job.getJobDataMap().put(StringDef.ARGS, args);
        Date start = new Date(System.currentTimeMillis() + delay);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(StringDef.TRIGGER + id).startAt(start).build();
        scheduler.scheduleJob(job, trigger);
        return id;
    }

    /**
     * 提交延时任务,该方法提交的任务为多次任务,该任务不会添加至服务缓存，需要客户端自己管理
     *
     * @param args     需要交给任务的初始化数据
     * @param delay    延时时间
     * @param interval 任务间隔,单位毫秒
     * @param loop     任务执行次数
     * @return id 任务id
     */
    public long submitDelayedTimeWork(Class<? extends BaseTimerWork> clazz, int delay, int interval, int loop, Object... args) throws SchedulerException {
        long id = identification.incrementAndGet();
        JobDetail job = JobBuilder.newJob(clazz).withIdentity(StringDef.TIMER_JOB + id).build();
        job.getJobDataMap().put(StringDef.ARGS, args);
        Trigger trigger;
        if (delay == 0) {
            trigger = TriggerBuilder.newTrigger().withIdentity(StringDef.TRIGGER + id).startNow().withSchedule(
                    SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(loop))
                    .build();
        } else {
            Date startDate = new Date(System.currentTimeMillis() + delay);
            trigger = TriggerBuilder.newTrigger().withIdentity(StringDef.TRIGGER + id).startAt(startDate).withSchedule(
                    SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(loop))
                    .build();
        }
        scheduler.scheduleJob(job, trigger);
        return id;
    }
}
