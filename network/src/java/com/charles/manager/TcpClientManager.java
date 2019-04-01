package com.charles.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author CharlesLee
 */
public class TcpClientManager {
    private static final Logger LOGGER = LogManager.getLogger(TcpClientManager.class);
    private static final TcpClientManager TCP_CLIENT_MANAGER = new TcpClientManager();

    private TcpClientManager() {
    }

    public static TcpClientManager getInstance() {
        return TCP_CLIENT_MANAGER;
    }
}
