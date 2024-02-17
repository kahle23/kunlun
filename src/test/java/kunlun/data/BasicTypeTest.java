/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.animal.Dog;
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
