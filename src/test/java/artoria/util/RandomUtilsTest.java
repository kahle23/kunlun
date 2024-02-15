package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.constant.Numbers.*;

public class RandomUtilsTest {
    private static Logger log = LoggerFactory.getLogger(RandomUtilsTest.class);

    @Test
    public void testNextInt() {
        for (int i = ZERO; i < ONE_HUNDRED; i++) {
            log.info("{}", RandomUtils.nextInt());
            log.info("{}", RandomUtils.nextInt(ONE_HUNDRED));
        }
    }

    @Test
    public void testNextLong() {
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("{}", RandomUtils.nextLong());
        }
    }

    @Test
    public void testNextFloat() {
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("{}", RandomUtils.nextFloat());
        }
    }

    @Test
    public void testNextDouble() {
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("{}", RandomUtils.nextDouble());
        }
    }

    @Test
    public void testNextBoolean() {
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("{}", RandomUtils.nextBoolean());
        }
    }

}
