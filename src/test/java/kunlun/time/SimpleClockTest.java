/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.time;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import static kunlun.common.constant.Numbers.TWENTY;
import static kunlun.common.constant.Numbers.ZERO;

public class SimpleClockTest {
    private static Logger log = LoggerFactory.getLogger(SimpleClockTest.class);

    @Test
    public void test1() {
        SimpleClock clock = new SimpleClock();
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("Time in millis: {}", clock.getTime());
        }
    }

}
