package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.lang.reflect.Array;

import static artoria.common.Constants.TEN;
import static artoria.common.Constants.ZERO;

public class ClassUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ClassUtilsTest.class);

    @Test
    public void test1() {
        byte[] bytes = new byte[TEN];
        log.info("{}", bytes.getClass());
        String[] strings = new String[TEN];
        log.info("{}", strings.getClass());
        log.info("{}", Array.newInstance(String.class, ZERO).getClass());
    }

}
