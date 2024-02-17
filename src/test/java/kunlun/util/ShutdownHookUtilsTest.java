/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import org.junit.Test;

public class ShutdownHookUtilsTest {

    @Test
    public void test1() {
        ShutdownHookUtils.addRunnable(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Shutdown Hook 1");
            }
        });
        System.out.println("test1");
        ShutdownHookUtils.addRunnable(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Shutdown Hook 2");
            }
        });
        System.out.println("test1 - test1");
    }

}
