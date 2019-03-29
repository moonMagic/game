package com.charles;

import com.charles.manager.ProjectManager;

/**
 * @author CharlesLee
 */
public class StartServer {
    public static void main(String[] args) {
        ProjectManager.getInstance().init("classpath:spring-config.xml");

    }
}
