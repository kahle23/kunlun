package sabertest.logger;

import org.junit.Test;
import saber.logger.JulUtils;

public class JdkLoggerTest {

    @Test
    public void test() {
        JulUtils.debug("Hello, World! ");
        JulUtils.info("Hello, World! ");
        JulUtils.warn("Hello, World! ");
        JulUtils.error("Hello, World! ");
        JulUtils.fatal("Hello, World! ");
    }

}
