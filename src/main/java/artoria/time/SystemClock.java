package artoria.time;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Use a separate thread to get the timestamp
 *      of the current time and cache it.
 * @see System#currentTimeMillis()
 * @author Kahle
 */
public class SystemClock implements Clock {
    private static final String THREAD_NAME = "system-clock";
    private final ScheduledThreadPoolExecutor executor;
    private volatile long nowTime;

    private SystemClock(long period) {
        this.nowTime = System.currentTimeMillis();
        this.executor = new ScheduledThreadPoolExecutor(
                1, new ClockThreadFactory()
        );
        this.executor.scheduleAtFixedRate(
                new ClockRunnable(), period, period, MILLISECONDS
        );
    }

    @Override
    public long getTime() {

        return this.nowTime;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (executor != null) {
            executor.shutdown();
        }
    }

    private class ClockThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, THREAD_NAME);
            thread.setDaemon(true);
            return thread;
        }
    }

    private class ClockRunnable implements Runnable {
        @Override
        public void run() {

            nowTime = System.currentTimeMillis();
        }
    }

    private static class Holder {

        private static final SystemClock INSTANCE = new SystemClock(1);
    }

    public static Clock getInstance() {

        return Holder.INSTANCE;
    }

}
