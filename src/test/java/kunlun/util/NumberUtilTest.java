/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.math.BigDecimal;

public class NumberUtilTest {
    private static Logger log = LoggerFactory.getLogger(NumberUtilTest.class);

    @Test
    public void test1() {
        log.info("{}", NumberUtil.round(578.4000345f));
        log.info("{}", NumberUtil.round(578.44325f).doubleValue() + 666);
        log.info("{}", NumberUtil.round(1578.478545f));
        log.info("{}", NumberUtil.round(12578.4455f));
        double round = NumberUtil.round(123578.4455f).doubleValue();
        BigDecimal decimal = new BigDecimal(round);
        log.info("{}", decimal);
        decimal = BigDecimal.valueOf(round);
        log.info("{}", decimal);
    }

    @Test
    public void test2() {
        log.info(NumberUtil.format(99, "00000"));
        log.info(NumberUtil.format(8754.65638, ".000"));
        log.info(NumberUtil.format(8754.65638, "0000000.000"));
        log.info(NumberUtil.format(8754.60000, ".000"));
        log.info(NumberUtil.format(99, "#######"));
        log.info(NumberUtil.format(8754.65638, "#######.###"));
        log.info(NumberUtil.format(8754.60000, "#######.##"));
        log.info(NumberUtil.format(new BigDecimal("0.012003"), ".000"));
        log.info(NumberUtil.format(new BigDecimal("0.012003"), "0.000"));
        log.info(NumberUtil.format(new BigDecimal("9.012003"), "0.000"));
        log.info(NumberUtil.format(new BigDecimal("99.012003"), "0.000"));
        log.info(NumberUtil.format(new BigDecimal("689.012003"), "0.000"));
    }

}
