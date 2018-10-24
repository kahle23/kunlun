package artoria.lifecycle;

import artoria.exception.ExceptionUtils;
import org.junit.Test;

import java.util.logging.Logger;

public class LifecycleUtilsTest {
    private static Logger log = Logger.getLogger(LifecycleUtilsTest.class.getName());
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
