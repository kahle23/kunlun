package artoria.lifecycle;

import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class LifecycleUtilsTest {
    private static Logger log = LoggerFactory.getLogger(LifecycleUtilsTest.class);
    private LifecycleTestBean bean = new LifecycleTestBean();

    @Test
    public void test1() {
        try {
            LifecycleUtils.initialize(bean);
        }
        catch (LifecycleException e) {
            log.info(ExceptionUtils.toString(e));
        }
    }

    @Test
    public void test2() {

        LifecycleUtils.destroy(bean);
    }

}
