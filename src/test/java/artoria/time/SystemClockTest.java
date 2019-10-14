package artoria.time;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.Constants.TWENTY;
import static artoria.common.Constants.ZERO;

public class SystemClockTest {
    private static Logger log = LoggerFactory.getLogger(SystemClockTest.class);

    @Test
    public void test1() {
        Clock clock = new SystemClock();
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("SystemClock: {}", clock.getTime());
        }
    }

}
