package com.charles.manager;

import com.charles.utils.ClassUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author CharlesLee
 */
public final class ProjectManager {

    private static final ProjectManager PROJECT_MANAGER = new ProjectManager();

    /**
     * 获取默认的类加载器
     */
    private final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

    private ApplicationContext context;

    private ProjectManager() {

    }

    public static ProjectManager getInstance() {
        return PROJECT_MANAGER;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void init(String... configs) {
        context = new FileSystemXmlApplicationContext(configs);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
