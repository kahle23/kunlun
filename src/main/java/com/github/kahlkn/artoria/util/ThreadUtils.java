package com.github.kahlkn.artoria.util;

import com.github.kahlkn.artoria.exception.ExceptionUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Thread tools.
 * @author Kahle
 */
public class ThreadUtils {

    public static void sleepQuietly(long millis) {
        Assert.state(millis > 0, "Parameter \"millis\" must greater than 0. ");
        try {
            Thread.sleep(millis);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static String getThreadName() {
        Thread thread = Thread.currentThread();
        return thread.getName();
    }

    public static ThreadInfo getThreadInfo(long threadId) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        return threadMXBean.getThreadInfo(threadId);
    }

}
