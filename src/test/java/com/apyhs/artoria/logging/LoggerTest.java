package com.apyhs.artoria.logging;

import org.junit.Test;

public class LoggerTest {
    private static Logger log = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void testLogMsg() {
        System.err.println("Test logger message, and show \"using logger: artoria.logging.JdkLoggerAdapter\".");
        log.trace("Hello, World!");
        log.debug("Hello, World!");
        log.info("Hello, World!");
        log.warn("Hello, World!");
        log.error("Hello, World!");
        System.err.println("set level INFO");
        LoggerFactory.setLevel(Level.INFO);
        log.trace("Hello, World!");
        log.debug("Hello, World!");
        log.info("Hello, World!");
        log.warn("Hello, World!");
        log.error("Hello, World!");
    }

    @Test
    public void testLogException() {
        System.err.println("Test logger RuntimeException, and show \"using logger: artoria.logging.JdkLoggerAdapter\".");
        log.trace("Hello, World!", new RuntimeException());
        log.debug("Hello, World!", new RuntimeException());
        log.info("Hello, World!", new RuntimeException());
        log.warn("Hello, World!", new RuntimeException());
        log.error("Hello, World!", new RuntimeException());
    }

}
