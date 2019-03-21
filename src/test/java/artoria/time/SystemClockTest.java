package artoria.time;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.Constants.BLANK_SPACE;

public class SystemClockTest {
    private static Logger log = LoggerFactory.getLogger(SystemClockTest.class);
    private static Clock systemClock = SystemClock.getInstance();

    private void testPerformance() {
        StringBuilder builder = new StringBuilder();
        int count = 50;

        builder.setLength(0);
        builder.append("SystemClock: ");
        for (int i = 0; i < count; i++) {
            long start = System.nanoTime();
            systemClock.getTime();
            long end = System.nanoTime();
            long tmp = end - start;
            if (tmp == 0) { continue; }
            builder.append(tmp);
            builder.append(BLANK_SPACE);
        }
        log.info(builder.toString());

        builder.setLength(0);
        builder.append("System: ");
        for (int i = 0; i < count; i++) {
            long start = System.nanoTime();
            System.currentTimeMillis();
            long end = System.nanoTime();
            long tmp = end - start;
            if (tmp == 0) { continue; }
            builder.append(tmp);
            builder.append(BLANK_SPACE);
        }
        log.info(builder.toString());

        log.info("System: {}", System.currentTimeMillis());
        log.info("SystemClock: {}", systemClock.getTime());
    }

    @Test
    public void test1() {
        for (int i = 0; i < 20; i++) {
            this.testPerformance();
        }
    }

}
