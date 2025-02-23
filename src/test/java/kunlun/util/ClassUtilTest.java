/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.animal.Cat;
import kunlun.test.pojo.entity.animal.Dog;
import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Date;

import static kunlun.common.constant.Numbers.TEN;
import static kunlun.common.constant.Numbers.ZERO;

public class ClassUtilTest {
    private static Logger log = LoggerFactory.getLogger(ClassUtilTest.class);

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
        log.info("{}", ClassUtil.getWrapper(int.class));
        log.info("{}", ClassUtil.getWrapper(long.class));
        log.info("{}", ClassUtil.getWrapper(void.class));
        log.info("{}", ClassUtil.getWrapper(Dog.class));
    }

    @Test
    public void test3() {
        log.info("{}", ClassUtil.getPrimitive(Integer.class));
        log.info("{}", ClassUtil.getPrimitive(Character.class));
        log.info("{}", ClassUtil.getPrimitive(void.class));
        log.info("{}", ClassUtil.getPrimitive(Void.class));
        log.info("{}", ClassUtil.getPrimitive(Dog.class));
    }

    @Test
    public void test4() {
        log.info("{}", ClassUtil.isSimpleValueType(BigDecimal.class));
        log.info("{}", ClassUtil.isSimpleValueType(Cat.class));
        log.info("{}", ClassUtil.isSimpleValueType(Dog.class));
        log.info("{}", ClassUtil.isSimpleValueType(String.class));
        log.info("{}", ClassUtil.isSimpleValueType(Date.class));
        log.info("{}", ClassUtil.isSimpleValueType(ClassUtil.class));
        log.info("{}", ClassUtil.isSimpleValueType(Boolean.class));
        log.info("{}", ClassUtil.isSimpleValueType(Number.class));
        log.info("{}", ClassUtil.isSimpleValueType(Character.class));
        log.info("{}", ClassUtil.isSimpleValueType(Class.class));
    }

}
