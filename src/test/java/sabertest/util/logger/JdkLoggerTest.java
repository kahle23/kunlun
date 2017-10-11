package sabertest.util.logger;

import org.junit.Test;
import saber.util.logger.JdkLogger;

public class JdkLoggerTest {

    @Test
    public void test() {
        JdkLogger.debug("Hello, World! ");
        JdkLogger.info("Hello, World! ");
        JdkLogger.warn("Hello, World! ");
        JdkLogger.error("Hello, World! ");
        JdkLogger.fatal("Hello, World! ");
    }

}
