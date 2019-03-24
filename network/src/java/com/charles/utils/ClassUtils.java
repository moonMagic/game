package com.charles.utils;

/**
 * @author charlesLee
 */
public class ClassUtils {

    /**
     * 获取默认的类加载器
     *
     * @author charlesLee
     **/
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loader == null) {
            loader = ClassUtils.class.getClassLoader();
            if (loader == null) {
                loader = ClassLoader.getSystemClassLoader();
            }
        }
        return loader;
    }
}
