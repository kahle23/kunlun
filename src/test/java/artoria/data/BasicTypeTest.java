package artoria.data;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.test.bean.Dog;
import org.junit.Test;

public class BasicTypeTest {
    private static Logger log = LoggerFactory.getLogger(BasicTypeTest.class);

    @Test
    public void test1() {
        log.info("{}", BasicType.isPrimitive(int.class));
        log.info("{}", BasicType.isPrimitive(long.class));
        log.info("{}", BasicType.isPrimitive(void.class));
        log.info("{}", BasicType.isPrimitive(Dog.class));
    }

    @Test
    public void test2() {
        log.info("{}", BasicType.isWrapper(Integer.class));
        log.info("{}", BasicType.isWrapper(Character.class));
        log.info("{}", BasicType.isWrapper(void.class));
        log.info("{}", BasicType.isWrapper(Void.class));
        log.info("{}", BasicType.isWrapper(Dog.class));
    }

    @Test
    public void test3() {
        log.info("{}", BasicType.getPrimitive(Integer.class));
        log.info("{}", BasicType.getPrimitive(Character.class));
        log.info("{}", BasicType.getPrimitive(void.class));
        log.info("{}", BasicType.getPrimitive(Void.class));
        log.info("{}", BasicType.getPrimitive(Dog.class));
    }

    @Test
    public void test4() {
        log.info("{}", BasicType.getWrapper(int.class));
        log.info("{}", BasicType.getWrapper(long.class));
        log.info("{}", BasicType.getWrapper(void.class));
        log.info("{}", BasicType.getWrapper(Dog.class));
    }

    @Test
    public void test5() {
        log.info("{}", BasicType.parse(int.class));
        log.info("{}", BasicType.parse(long.class));
        log.info("{}", BasicType.parse(void.class));
        log.info("{}", BasicType.parse(Dog.class));
        log.info("{}", BasicType.parse(Integer.class));
        log.info("{}", BasicType.parse(Boolean.class));
    }

}
