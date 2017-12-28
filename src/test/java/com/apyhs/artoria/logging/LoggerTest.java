package com.apyhs.artoria.logging;

import com.apyhs.artoria.exception.UnexpectedException;
import org.junit.Test;

public class LoggerTest {
    private static Logger log = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void logMessage() {
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
    public void logException() {
        System.err.println("Test logger RuntimeException, and show \"using logger: com.apyhs.artoria.logging.JdkLoggerAdapter\".");
        log.trace("Hello, World!", new UnexpectedException());
        log.debug("Hello, World!", new UnexpectedException());
        log.info("Hello, World!", new UnexpectedException());
        log.warn("Hello, World!", new UnexpectedException());
        log.error("Hello, World!", new UnexpectedException());
    }

}
