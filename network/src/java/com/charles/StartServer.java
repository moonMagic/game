package com.charles;

import com.charles.manager.ProjectManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author CharlesLee
 */
public class StartServer {
    public static void main(String[] args) {
        ProjectManager.getInstance().init("classpath:spring-config.xml");
    }
}
