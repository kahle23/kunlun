/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.bean;

import com.alibaba.fastjson.JSON;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.mock.MockUtils;
import kunlun.test.pojo.entity.animal.Cat;
import kunlun.test.pojo.entity.animal.Dog;
import org.junit.Test;

import java.util.Arrays;

/**
 * The bean holder Test.
 * @author Kahle
 */
public class BeanHolderTest {
    private static final Logger log = LoggerFactory.getLogger(BeanHolderTest.class);

    @Test
    public void test1() {
        Dog dog = MockUtils.mock(Dog.class);
        Cat cat = MockUtils.mock(Cat.class);
        log.info("dog is: {}", JSON.toJSONString(dog));
        log.info("cat is: {}", JSON.toJSONString(cat));
        BeanHolder.put("dog", dog);
//        BeanHolder.put("dog", cat);
        BeanHolder.put("dog1", dog);
        BeanHolder.put("cat", cat);
        BeanHolder.put("cat1", cat);
        //log.info(JSON.toJSONString(BeanHolder.get(Dog.class)));
//        Cat cat = BeanHolder.get("dog", Cat.class);
//        log.info(JSON.toJSONString(cat));
        log.info("Dog names: {}", Arrays.toString(BeanHolder.getNames(Dog.class)));
        log.info("Cat names: {}", Arrays.toString(BeanHolder.getNames(Cat.class)));
    }

}
