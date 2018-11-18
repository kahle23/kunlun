package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.lang.reflect.Array;

public class ClassUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ClassUtilsTest.class);

    @Test
    public void test1() {
        byte[] bytes = new byte[10];
        log.info("" + bytes.getClass());
        String[] strings = new String[10];
        log.info("" + strings.getClass());
        log.info("" + Array.newInstance(String.class, 0).getClass());
    }

}
