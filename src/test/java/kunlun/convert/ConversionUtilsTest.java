/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert;

import com.alibaba.fastjson.JSON;
import kunlun.convert.support.CatToDogConverter;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.mock.MockUtils;
import kunlun.test.pojo.entity.animal.Cat;
import kunlun.test.pojo.entity.animal.Dog;
import kunlun.time.DateUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static kunlun.util.ObjectUtils.cast;

/**
 * The conversion tools Test.
 * @author Kahle
 */
public class ConversionUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ConversionUtilsTest.class);

    @Test
    public void testIntToPrimitiveDouble() {
        int src = 102;
        Object obj = ConversionUtils.convert(src, double.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToPrimitiveDouble() {
        String src = "102";
        Object obj = ConversionUtils.convert(src, double.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToBoolean() {
        String src = "true";
        Object obj = ConversionUtils.convert(src, Boolean.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testPrimitiveBooleanToString() {
        Object obj = ConversionUtils.convert(true, String.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testDateToTimestamp() {
        Object obj = ConversionUtils.convert(new Date(), Timestamp.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToDate() {
        Object obj;
        obj = ConversionUtils.convert("2019-03-25 10:10:10 300", java.sql.Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = ConversionUtils.convert("2019-03-25T10:10:10.300+0800", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = ConversionUtils.convert("2019-03-25 10:10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = ConversionUtils.convert("2019-03-25 10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = ConversionUtils.convert("2019-03-25", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = ConversionUtils.convert("2019/03/25 10:10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = ConversionUtils.convert("2019/03/25 10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = ConversionUtils.convert("2019/03/25", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = ConversionUtils.convert("test2019/03/25error", null, Date.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testLongStringToDate() {
        Object obj = ConversionUtils.convert(String.valueOf(DateUtils.getTimeInMillis()), java.sql.Date.class);
        log.info("{} {}", obj.getClass(), obj);
        Object obj1 = ConversionUtils.convert("-45674576567", java.sql.Date.class);
        log.info("{} {}", obj1.getClass(), obj1);
    }

    @Test
    public void testCatToDog() {
        ConversionUtils.registerConverter(new CatToDogConverter(ConversionUtils.getConversionService()));
        Cat cat = MockUtils.mock(Cat.class);
        log.info("{}", JSON.toJSONString(cat));
        Dog dog = cast(ConversionUtils.convert(cat, Dog.class));
        log.info("{}", JSON.toJSONString(dog));
    }

}
