/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util.concurrent;

import kunlun.util.Assert;

import java.util.ServiceLoader;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * The thread pool tools.
 * @author Kahle
 */
public class ThreadPoolUtil {
    private static volatile ThreadPoolWrapper wrapper;

    public static ThreadPoolWrapper getWrapper() {
        if (wrapper != null) { return wrapper; }
        synchronized (ThreadPoolUtil.class) {
            if (wrapper != null) { return wrapper; }
            ThreadPoolUtil.setWrapper(new ThreadPoolWrapper() {
                @Override
                public Executor wrap(Executor executor) { return executor; }
            });
            return wrapper;
        }
    }

    public static void setWrapper(ThreadPoolWrapper wrapper) {

        ThreadPoolUtil.wrapper = Assert.notNull(wrapper);
    }

    public static ScheduledExecutorService wrap(ScheduledExecutorService executorService) {

        return (ScheduledExecutorService) getWrapper().wrap(executorService);
    }

    public static ExecutorService wrap(ExecutorService executorService) {

        return (ExecutorService) getWrapper().wrap(executorService);
    }

    public static Executor wrap(Executor executor) {

        return getWrapper().wrap(executor);
    }

    static {
        ServiceLoader<ThreadPoolWrapper> loader = ServiceLoader.load(ThreadPoolWrapper.class);
        ThreadPoolWrapper wpr = null;
        for (ThreadPoolWrapper item : loader) {
            if (item == null) { continue; }
            if (wpr == null) { wpr = item; }
        }
        if (wpr != null) { setWrapper(wpr); }
    }

}
