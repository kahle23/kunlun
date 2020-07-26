package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.math.BigDecimal;

public class NumberUtilsTest {
    private static Logger log = LoggerFactory.getLogger(NumberUtilsTest.class);

    @Test
    public void test1() {
        log.info("{}", NumberUtils.round(578.4000345f));
        log.info("{}", NumberUtils.round(578.44325f).doubleValue() + 666);
        log.info("{}", NumberUtils.round(1578.478545f));
        log.info("{}", NumberUtils.round(12578.4455f));
        double round = NumberUtils.round(123578.4455f).doubleValue();
        BigDecimal decimal = new BigDecimal(round);
        log.info("{}", decimal);
        decimal = BigDecimal.valueOf(round);
        log.info("{}", decimal);
    }

    @Test
    public void test2() {
        log.info(NumberUtils.format(99, "00000"));
        log.info(NumberUtils.format(8754.65638, ".000"));
        log.info(NumberUtils.format(8754.65638, "0000000.000"));
        log.info(NumberUtils.format(8754.60000, ".000"));
        log.info(NumberUtils.format(99, "#######"));
        log.info(NumberUtils.format(8754.65638, "#######.###"));
        log.info(NumberUtils.format(8754.60000, "#######.##"));
        log.info(NumberUtils.format(new BigDecimal("0.012003"), ".000"));
        log.info(NumberUtils.format(new BigDecimal("0.012003"), "0.000"));
        log.info(NumberUtils.format(new BigDecimal("9.012003"), "0.000"));
        log.info(NumberUtils.format(new BigDecimal("99.012003"), "0.000"));
        log.info(NumberUtils.format(new BigDecimal("689.012003"), "0.000"));
    }

}
