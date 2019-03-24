package com.charles.utils;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author CharlesLee
 */
public class StringUtil {

    private static final ReentrantLock LOCK = new ReentrantLock();

    public static String getUUID() {
        try {
            LOCK.lock();
            return UUID.randomUUID().toString().replaceAll("-", "");
        } finally {
            LOCK.unlock();
        }
    }
}
