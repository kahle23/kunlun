package apyh.artoria.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Thread tools.
 * @author Kahle
 */
public class ThreadUtils {

    public static int getLineNumber() {
        Thread thread = Thread.currentThread();
        return thread.getStackTrace()[2].getLineNumber();
    }

    public static String getMethodName() {
        Thread thread = Thread.currentThread();
        return thread.getStackTrace()[2].getMethodName();
    }

    public static String getClassName() {
        Thread thread = Thread.currentThread();
        return thread.getStackTrace()[2].getClassName();
    }

    public static String getFileName() {
        Thread thread = Thread.currentThread();
        return thread.getStackTrace()[2].getFileName();
    }

    public static void sleepQuietly(long millis) {
        Assert.state(millis > 0, "Millis must greater than 0. ");
        try {
            Thread.sleep(millis);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getThreadName() {
        Thread thread = Thread.currentThread();
        return thread.getName();
    }

    public static ThreadInfo getThreadInfo(long threadId) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        Assert.notNull(threadMXBean, "Get ThreadMXBean failure. ");
        return threadMXBean.getThreadInfo(threadId);
    }

}
