package com.apyhs.artoria.logging;

import org.junit.Test;

import java.util.logging.Logger;

public class JdkLoggerPerformance {
    private static final Integer count = 20;
    private static final Integer loopCount = 100000;

    @Test
    public void test1() {
        Logger log = Logger.getLogger(JdkLoggerPerformance.class.getName());
        System.out.print("Jdk Logger: ");
        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < loopCount; j++) {
                log.finer("hello");
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();
    }

    @Test
    public void test2() {
        com.apyhs.artoria.logging.Logger log = LoggerFactory.getLogger(JdkLoggerPerformance.class);
        System.out.print("Jdk Logger1: ");
        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < loopCount; j++) {
                log.debug("hello");
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();
    }

}
