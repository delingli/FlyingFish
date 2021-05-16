package com.dc.commonlib.commonentity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static ThreadPool threadPool = new ThreadPool();

    public static ThreadPool getInstance() {
        return threadPool;
    }

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private ThreadPool() {
    }

    public ExecutorService getThreadPool() {
        return executorService;
    }
}
