package com.charles.entity;


import io.netty.util.Recycler;


/**
 * 可回收对象需要统一继承的父类
 *
 * @author CharlesLee
 */
public abstract class BaseRecyclable implements Entity {

    protected Recycler.Handle<BaseRecyclable> handle;

    /**
     * 判断该对象是否在使用中
     * 如果值为true,那么证明该对象正在使用中, startUpTime 值不为0
     * 如果值为false,那么证明该对象已经使用完毕了, startUpTime 值将归0
     */
    protected boolean inUse;

    /**
     * 该对象开始使用的时间
     */
    protected long startUpTime;

    public void autoRecycle() {
        handle.recycle(this);
        inUse = false;
        startUpTime = 0;
        recycle();
    }

    /**
     * 对象的成员自动置空的方法
     */
    protected abstract void recycle();

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public long getStartUpTime() {
        return startUpTime;
    }

    public void setStartUpTime(long startUpTime) {
        this.startUpTime = startUpTime;
    }

    public BaseRecyclable setHandle(Recycler.Handle<BaseRecyclable> handle) {
        this.handle = handle;
        return this;
    }
}
