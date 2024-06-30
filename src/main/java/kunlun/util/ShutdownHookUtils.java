/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.util.thread.CombinedRunnable;
import kunlun.util.thread.ExecutorServiceCleaner;

import java.util.concurrent.ExecutorService;

/**
 * The shutdown hook tools.
 * @author Kahle
 */
public class ShutdownHookUtils {
    private static final ExecutorServiceCleaner EXECUTOR_SERVICE_CLEANER;
    private static final CombinedRunnable COMBINED_RUNNABLE;

    static {
        EXECUTOR_SERVICE_CLEANER = new ExecutorServiceCleaner();
        EXECUTOR_SERVICE_CLEANER.setIgnoreException(true);
        EXECUTOR_SERVICE_CLEANER.setShutdownNow(false);
        COMBINED_RUNNABLE = new CombinedRunnable();
        COMBINED_RUNNABLE.setIgnoreException(true);
        COMBINED_RUNNABLE.add(EXECUTOR_SERVICE_CLEANER);
        String threadName = "shutdown-hook-executor";
        Thread thread = new Thread(COMBINED_RUNNABLE, threadName);
        Runtime.getRuntime().addShutdownHook(thread);
    }

    public static void addRunnable(Runnable runnable) {
        Assert.notNull(runnable, "Parameter \"runnable\" must not null. ");
        COMBINED_RUNNABLE.add(runnable);
    }

    public static void removeRunnable(Runnable runnable) {
        Assert.notNull(runnable, "Parameter \"runnable\" must not null. ");
        COMBINED_RUNNABLE.remove(runnable);
    }

    public static void addExecutorService(ExecutorService executorService) {
        Assert.notNull(executorService, "Parameter \"executorService\" must not null. ");
        EXECUTOR_SERVICE_CLEANER.add(executorService);
    }

    public static void removeExecutorService(ExecutorService executorService) {
        Assert.notNull(executorService, "Parameter \"executorService\" must not null. ");
        EXECUTOR_SERVICE_CLEANER.remove(executorService);
    }

}
