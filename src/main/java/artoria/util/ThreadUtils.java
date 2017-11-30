package artoria.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author Kahle
 */
public class ThreadUtils {

    public static void sleepQuietly(long millis) {
        Assert.state(millis > 0, "Millis must greater than 0. ");
        try {
            Thread.sleep(millis);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ThreadInfo getThreadInfo(long threadId) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        Assert.state(threadMXBean != null, "Get ThreadMXBean failure. ");
        return threadMXBean.getThreadInfo(threadId);
    }

    public static int currentLineNumber() {
        Thread thread = Thread.currentThread();
        return thread.getStackTrace()[2].getLineNumber();
    }

    public static String currentMethodName() {
        Thread thread = Thread.currentThread();
        return thread.getStackTrace()[2].getMethodName();
    }

    public static String currentClassName() {
        Thread thread = Thread.currentThread();
        return thread.getStackTrace()[2].getClassName();
    }

    public static String currentFileName() {
        Thread thread = Thread.currentThread();
        return thread.getStackTrace()[2].getFileName();
    }

    public static String currentThreadName() {
        Thread thread = Thread.currentThread();
        return thread.getName();
    }

}
