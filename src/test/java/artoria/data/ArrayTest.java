package artoria.data;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

/**
 * The Array class Test.
 * @author Kahle
 */
public class ArrayTest {
    private static final Logger log = LoggerFactory.getLogger(ArrayTest.class);

    @Test
    public void test1() {
        Array of = Array.of(new Object[]{"1", "2", 3});
        log.info(String.valueOf(of));
        of = Array.of(new Integer[] {1, 1, 2, 2});
        log.info(String.valueOf(of));
    }

}
