package com.charles.manager;


import com.charles.entity.BaseRecyclable;
import io.netty.util.Recycler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 可回收对象工厂，单例饿汉模式
 * <p>
 * 该工厂将对外提供可回收对象的生产与回收
 * <p>
 * 需要注意的是该工厂管理的对象只可用于大量重复复用对象,
 * 如果目标对象需要保持长时间的存在,那么请勿使用该工厂创建的对象
 *
 * @author CharlesLee
 */
public final class RecyclableObjectFactory {

    private static final Logger LOGGER = LogManager.getLogger(RecyclableObjectFactory.class);

    private static final RecyclableObjectFactory FACTORY = new RecyclableObjectFactory();

    public static RecyclableObjectFactory getInstance() {
        return FACTORY;
    }

    /**
     * 创建可回收对象的缓存工具
     */
    private Map<Class<? extends BaseRecyclable>, Recycler<? extends BaseRecyclable>> cache = new HashMap<>();


    /**
     * 这里遍历并初始化可回收对象
     */
    @SuppressWarnings("unchecked")
    private RecyclableObjectFactory() {
        try {
            String[] allBeans = ProjectManager.getInstance().getContext().getBeanDefinitionNames();
            for (String beanName : allBeans) {
                final Class<?> clazz = ProjectManager.getInstance().getContext().getBean(beanName).getClass();
                // 这里判断为是否是可回收对象
                if (BaseRecyclable.class.isAssignableFrom(clazz)) {
                    Recycler<? extends BaseRecyclable> recycler = new Recycler<BaseRecyclable>() {
                        @Override
                        protected BaseRecyclable newObject(Handle<BaseRecyclable> handle) {
                            BaseRecyclable result = null;
                            try {
                                result = ((BaseRecyclable) clazz.getDeclaredConstructor().newInstance()).setHandle(handle);
                            } catch (Exception e) {
                                e.printStackTrace();
                                // 这里永远不会发生异常
                            }
                            return result;
                        }
                    };
                    cache.put((Class<? extends BaseRecyclable>) clazz, recycler);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 这里永远不会发生异常
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T get(Class<? extends BaseRecyclable> clazz) {
        Recycler<? extends BaseRecyclable> recycler = cache.get(clazz);
        if (recycler == null) {
            throw new RuntimeException(clazz.getName()  + "不是一个可回收对象, 请检查该类是否继承 : com.charles.entity.BaseRecyclable ");
        }
        synchronized (recycler) {
            BaseRecyclable result = recycler.get();
            result.setInUse(true);
            result.setStartUpTime(System.currentTimeMillis());
            return (T) result;
        }
    }

    public void recycle(BaseRecyclable o) {
        if (o == null) {
            return;
        }
        if (o.isInUse()) {
            o.autoRecycle();
        }
    }
}
