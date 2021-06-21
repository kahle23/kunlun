package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.test.bean.Dog;
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

    @Test
    public void test2() {
        log.info("{}", ClassUtils.getWrapper(int.class));
        log.info("{}", ClassUtils.getWrapper(long.class));
        log.info("{}", ClassUtils.getWrapper(void.class));
        log.info("{}", ClassUtils.getWrapper(Dog.class));
    }

    @Test
    public void test3() {
        log.info("{}", ClassUtils.getPrimitive(Integer.class));
        log.info("{}", ClassUtils.getPrimitive(Character.class));
        log.info("{}", ClassUtils.getPrimitive(void.class));
        log.info("{}", ClassUtils.getPrimitive(Void.class));
        log.info("{}", ClassUtils.getPrimitive(Dog.class));
    }

}
