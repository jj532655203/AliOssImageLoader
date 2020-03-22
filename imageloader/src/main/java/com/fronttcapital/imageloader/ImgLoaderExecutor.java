package com.fronttcapital.imageloader;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Jay
 * 工作线程池(用于大量加载图片)
 */

public class ImgLoaderExecutor {

    private static final int INITIAL_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 1;

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 10;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final ThreadPoolExecutor threadPoolExecutor;

    private static ImgLoaderExecutor mJobExecutor;

    private ImgLoaderExecutor() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(4);
        this.threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public static ImgLoaderExecutor getInstance() {
        if (mJobExecutor == null) {
            synchronized (ImgLoaderExecutor.class) {
                if (mJobExecutor == null) {
                    mJobExecutor = new ImgLoaderExecutor();
                }
            }
        }
        return mJobExecutor;
    }

    public void execute(Runnable runnable) {
        this.threadPoolExecutor.execute(runnable);
    }

}
