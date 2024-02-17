/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import static kunlun.common.constant.Numbers.*;

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
