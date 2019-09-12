package artoria.time;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class SystemClockTest {
    private static Logger log = LoggerFactory.getLogger(SystemClockTest.class);

    @Test
    public void test1() {
        Clock clock = new SystemClock();
        for (int i = 0; i < 20; i++) {
            log.info("SystemClock: {}", clock.getTime());
        }
    }

}
