package artoria.time;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.constant.Numbers.TWENTY;
import static artoria.common.constant.Numbers.ZERO;

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
