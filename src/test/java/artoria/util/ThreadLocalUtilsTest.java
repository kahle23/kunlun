package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class ThreadLocalUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ThreadLocalUtilsTest.class);
    private String testKey = "Test_Key";

    private void doGetFromThreadLocal() {
        Object value = ThreadLocalUtils.getValue(testKey);
        log.info("{}", value);
    }

    @Test
    public void testSetGet() {
        ThreadLocalUtils.setValue(testKey, "rt.jar");
        doGetFromThreadLocal();
    }

    @Test
    public void testRemove() {
        ThreadLocalUtils.setValue(testKey, "rt.jar");
        doGetFromThreadLocal();
        ThreadLocalUtils.remove(testKey);
        doGetFromThreadLocal();
    }

    @Test
    public void testClear() {
        ThreadLocalUtils.setValue(testKey, "rt.jar");
        doGetFromThreadLocal();
        ThreadLocalUtils.clear();
        doGetFromThreadLocal();
        ThreadLocalUtils.setValue(testKey, "rt.jar");
        doGetFromThreadLocal();
    }

}
