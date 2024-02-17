/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.thread;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

public class CombinedRunnableTest {
    private static Logger log = LoggerFactory.getLogger(CombinedRunnableTest.class);

    @Test
    public void test1() {
        CombinedRunnable compositeRunnable = new CombinedRunnable();
        compositeRunnable.add(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Runnable 10");
            }
        });
        compositeRunnable.add(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Runnable 2");
            }
        });
        compositeRunnable.add(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Runnable 5");
            }
        });
        compositeRunnable.add(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Runnable -100");
            }
        });
        compositeRunnable.run();
    }

}
