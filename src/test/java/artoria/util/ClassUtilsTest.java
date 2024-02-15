package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.test.pojo.entity.animal.Cat;
import artoria.test.pojo.entity.animal.Dog;
import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Date;

import static artoria.common.constant.Numbers.TEN;
import static artoria.common.constant.Numbers.ZERO;

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

    @Test
    public void test4() {
        log.info("{}", ClassUtils.isSimpleValueType(BigDecimal.class));
        log.info("{}", ClassUtils.isSimpleValueType(Cat.class));
        log.info("{}", ClassUtils.isSimpleValueType(Dog.class));
        log.info("{}", ClassUtils.isSimpleValueType(String.class));
        log.info("{}", ClassUtils.isSimpleValueType(Date.class));
        log.info("{}", ClassUtils.isSimpleValueType(ClassUtils.class));
        log.info("{}", ClassUtils.isSimpleValueType(Boolean.class));
        log.info("{}", ClassUtils.isSimpleValueType(Number.class));
        log.info("{}", ClassUtils.isSimpleValueType(Character.class));
        log.info("{}", ClassUtils.isSimpleValueType(Class.class));
    }

}
