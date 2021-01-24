/**
 *
 */
package com.data.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
    // cached thread pool
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static ExecutorService getThreadPool() {
        return threadPool;
    }

    // clear cached thread pool
    private static ExecutorService clearCachedThreadPool = Executors.newCachedThreadPool();

    public static ExecutorService getclearCachedThreadPool() {
        return clearCachedThreadPool;
    }
}
