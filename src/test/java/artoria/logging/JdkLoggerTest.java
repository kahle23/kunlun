package artoria.logging;

import artoria.exception.UncheckedException;
import org.junit.Test;

public class JdkLoggerTest {
    private static Logger log = LoggerFactory.getLogger(JdkLoggerTest.class);

    @Test
    public void logMessage() {
        System.err.println("Test logger message. ");
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
        System.err.println("Test logger RuntimeException. ");
        log.trace("Hello, World!", new UncheckedException());
        log.debug("Hello, World!", new UncheckedException());
        log.info("Hello, World!", new UncheckedException());
        log.warn("Hello, World!", new UncheckedException());
        log.error("Hello, World!", new UncheckedException());
    }

}
