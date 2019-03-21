package artoria.logging;

import artoria.exception.UncheckedException;
import org.junit.Test;

public class JdkLoggerTest {
    private static Logger log = LoggerFactory.getLogger(JdkLoggerTest.class);

    @Test
    public void logMessage() {
        log.info(">>>>>>>> Test logger message. ");
        log.trace("Hello, World!");
        log.debug("Hello, World!");
        log.info("Hello, World!");
        log.warn("Hello, World!");
        log.error("Hello, World!");
        log.info(">>>>>>>> set level INFO. ");
        LoggerFactory.setLevel(Level.INFO);
        log.trace("Hello, World!");
        log.debug("Hello, World!");
        log.info("Hello, World!");
        log.warn("Hello, World!");
        log.error("Hello, World!");
    }

    @Test
    public void logException() {
        log.info(">>>>>>>> Test logger RuntimeException. ");
        log.trace("Hello, World! ", new UncheckedException());
        log.debug("Hello, World! ", new UncheckedException());
        log.info("Hello, World! ", new UncheckedException());
        log.warn("Hello, World! ", new UncheckedException());
        log.error("Hello, World! ", new UncheckedException());
    }

    @Test
    public void logFormat() {
        log.info(">>>>>>>> Test logger format. ");
        log.info("{}", "Hello, World! <<");
        log.info("Hello, {}! ", "World");
        log.info("Hello, {}, {}, {}! ", "111", "222", "333");
        log.info("Hello, {}, {}, {}! ", "aaa", "bbb");
        log.info("Hello, \\{}, \\\\{}, {}! ", "aaa", "bbb", "ccc");
        log.info("\\{}! ", "aaa");
        log.info("\\\\{}! ", "aaa");
        log.trace("Hello, {}, {}, {}! ", "111", "222", "333");
        log.debug("Hello, {}, {}, {}! ", "111", "222", "333");
        log.warn("Hello, {}, {}, {}! ", "111", "222", "333");
        log.error("Hello, {}, {}, {}! ", "111", "222", "333");
    }

}
