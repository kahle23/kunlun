package sabertest.logger;

import org.junit.Test;
import saber.logger.JdkLogger;

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
