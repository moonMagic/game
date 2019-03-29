package com.charles.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Charles Lee original, reprint please indicate the source
 *
 * @author CharlesLee
 */
@Configuration
@PropertySource("classpath:properties/system.properties")
public class SystemConfig {

    @Value("${server.type}")
    private int serverType;

    @Value("${server.manager.address}")
    private String serverManagerAddress;

    @Value("${server.manager.port}")
    private int serverManagerPort;

    @Value("${service.http.open}")
    private boolean serviceHttpOpen;

    @Value("${service.http.address}")
    private String serviceHttpAddress;

    @Value("${service.http.port}")
    private int serviceHttpPort;

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getServerManagerAddress() {
        return serverManagerAddress;
    }

    public void setServerManagerAddress(String serverManagerAddress) {
        this.serverManagerAddress = serverManagerAddress;
    }

    public int getServerManagerPort() {
        return serverManagerPort;
    }

    public void setServerManagerPort(int serverManagerPort) {
        this.serverManagerPort = serverManagerPort;
    }

    public boolean isServiceHttpOpen() {
        return serviceHttpOpen;
    }

    public void setServiceHttpOpen(boolean serviceHttpOpen) {
        this.serviceHttpOpen = serviceHttpOpen;
    }

    public String getServiceHttpAddress() {
        return serviceHttpAddress;
    }

    public void setServiceHttpAddress(String serviceHttpAddress) {
        this.serviceHttpAddress = serviceHttpAddress;
    }

    public int getServiceHttpPort() {
        return serviceHttpPort;
    }

    public void setServiceHttpPort(int serviceHttpPort) {
        this.serviceHttpPort = serviceHttpPort;
    }
}
