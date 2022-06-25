package artoria.thread;

import artoria.util.Assert;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static artoria.common.Constants.*;

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
