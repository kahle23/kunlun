/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.thread;

import kunlun.util.Assert;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.MINUS;

/**
 * The simple thread factory.
 * @author Kahle
 */
public class SimpleThreadFactory implements ThreadFactory {
    private final AtomicInteger counter;
    private final String threadNamePrefix;
    private final Boolean daemon;
    private final int stepLength;

    public SimpleThreadFactory(String threadNamePrefix, Boolean daemon) {

        this(threadNamePrefix, daemon, ONE, ONE);
    }

    public SimpleThreadFactory(String threadNamePrefix, Boolean daemon, int initialValue, int stepLength) {
        Assert.notBlank(threadNamePrefix, "Parameter \"threadNamePrefix\" must not blank. ");
        Assert.notNull(daemon, "Parameter \"daemon\" must not null. ");
        Assert.isTrue(initialValue >= ZERO, "Parameter \"initialValue\" must >= 0. ");
        Assert.isTrue(stepLength > ZERO, "Parameter \"stepLength\" must > 0. ");
        if (!threadNamePrefix.endsWith(MINUS)) {
            threadNamePrefix += MINUS;
        }
        this.counter = new AtomicInteger(initialValue);
        this.threadNamePrefix = threadNamePrefix;
        this.stepLength = stepLength;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Assert.notNull(runnable, "Parameter \"runnable\" must not null. ");
        int getAndAdd = counter.getAndAdd(stepLength);
        String threadName = threadNamePrefix + getAndAdd;
        Thread thread = new Thread(runnable, threadName);
        thread.setDaemon(daemon);
        return thread;
    }

}
