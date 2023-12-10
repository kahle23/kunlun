package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.util.Arrays;

/**
 * The array tools Test.
 * @author Kahle
 */
public class ArrayUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ArrayUtilsTest.class);

    @Test
    public void reverseTest1() {
        byte[] arr = new byte[]{1, 2, 3, 4, 5};
        ArrayUtils.reverse(arr);
        log.info(Arrays.toString(arr));
    }

    @Test
    public void reverseTest2() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        ArrayUtils.reverse(arr);
        log.info(Arrays.toString(arr));
    }

}
