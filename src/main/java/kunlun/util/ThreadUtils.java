/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.exception.ExceptionUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import static kunlun.common.constant.Numbers.ZERO;

/**
 * The thread tools.
 * @author Kahle
 */
public class ThreadUtils {

    public static void sleepQuietly(long millis) {
        Assert.isTrue(millis >= ZERO
                , "Parameter \"millis\" must greater than or equal to 0. ");
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
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        return mxBean.getThreadInfo(threadId);
    }

}
