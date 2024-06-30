/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert;

import com.alibaba.fastjson.JSON;
import kunlun.cache.Cache;
import kunlun.cache.CacheUtils;
import kunlun.cache.support.SimpleCache;
import kunlun.cache.support.SimpleCacheConfig;
import kunlun.convert.support.CatToDogConverter;
import kunlun.convert.support.ListBasicToListBasicConverter;
import kunlun.convert.support.NumberToDateConverter;
import kunlun.data.ReferenceType;
import kunlun.data.mock.MockUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.animal.Cat;
import kunlun.test.pojo.entity.animal.Dog;
import kunlun.test.pojo.entity.other.Book;
import kunlun.time.DateUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.util.ObjectUtils.cast;
import static kunlun.util.TypeUtils.parameterizedOf;

public class CacheConversionProviderTest {
    private static final Logger log = LoggerFactory.getLogger(CacheConversionProviderTest.class);
    private static final ConversionService conversionProvider;

    static {
        String cacheName="test";
        Cache cache = new SimpleCache(
                new SimpleCacheConfig(ReferenceType.SOFT, 3L, TimeUnit.MINUTES));
        CacheUtils.registerCache(cacheName, cache);
        conversionProvider = new CacheConversionService(new SimpleConversionService(), cacheName);
    }

    @Test
    public void testIntToPrimitiveDouble() {
        int src = 102;
        Object obj = conversionProvider.convert(src, double.class);
        log.info("{} {}", obj.getClass(), obj);
        obj = conversionProvider.convert(101, double.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToPrimitiveDouble() {
        String src = "102";
        Object obj = conversionProvider.convert(src, double.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToBoolean() {
        String src = "true";
        Object obj = conversionProvider.convert(src, Boolean.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testPrimitiveBooleanToString() {
        Object obj = conversionProvider.convert(true, String.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testDateToTimestamp() {
        Object obj = conversionProvider.convert(new Date(), Timestamp.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToDate() {
        Object obj;
        obj = conversionProvider.convert("2019-03-25 10:10:10 300", java.sql.Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = conversionProvider.convert("2019-03-25T10:10:10.300+0800", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = conversionProvider.convert("2019-03-25 10:10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = conversionProvider.convert("2019-03-25 10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = conversionProvider.convert("2019-03-25", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = conversionProvider.convert("2019/03/25 10:10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = conversionProvider.convert("2019/03/25 10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = conversionProvider.convert("2019/03/25", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = conversionProvider.convert("test2019/03/25error", null, Date.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testLongStringToDate() {
        Object obj = conversionProvider.convert(String.valueOf(DateUtils.getTimeInMillis()), java.sql.Date.class);
        log.info("{} {}", obj.getClass(), obj);
        Object obj1 = conversionProvider.convert("-45674576567", java.sql.Date.class);
        log.info("{} {}", obj1.getClass(), obj1);
    }

    @Test
    public void testNumberToDate() {
        long timeInMillis = DateUtils.getTimeInMillis();
        Date convert = cast(conversionProvider.convert(timeInMillis, Date.class));
        log.info("{}", DateUtils.format(convert));
        NumberToDateConverter numberToDateConverter = new NumberToDateConverter(conversionProvider);
        numberToDateConverter.setUnixTimestamp(true);
        conversionProvider.registerConverter(numberToDateConverter);
        convert = cast(conversionProvider.convert(timeInMillis, Date.class));
        log.info("{}", DateUtils.format(convert));
    }

    @Test
    public void testNumberToString() {
        long src = 102L;
        Object obj = conversionProvider.convert(src, String.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testCatToDog() {
        conversionProvider.registerConverter(new CatToDogConverter(conversionProvider));
        Cat cat = MockUtils.mock(Cat.class);
        log.info("{}", JSON.toJSONString(cat));
        Dog dog = cast(conversionProvider.convert(cat, Dog.class));
        log.info("{}", JSON.toJSONString(dog));
    }

    @Test
    public void testAddAndRemoveConverter() {
        GenericConverter converter1 = new ListBasicToListBasicConverter(conversionProvider);
        GenericConverter converter2 = new CatToDogConverter(conversionProvider);
        conversionProvider.registerConverter(converter1);
        conversionProvider.registerConverter(converter2);
        conversionProvider.deregisterConverter(converter1);
        conversionProvider.deregisterConverter(converter2);
    }

    @Test
    public void testListBasicToListBasic() {
        conversionProvider.registerConverter(new ListBasicToListBasicConverter(conversionProvider));
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, "10", "11", "12", "13", "14", "15");
        Object convert = conversionProvider.convert(list,
                parameterizedOf(List.class, String.class),
                parameterizedOf(List.class, Integer.class));
        log.info("{}", convert.getClass());
        log.info("{}", convert);
        log.info("{}", ((List) convert).get(ZERO).getClass());
    }

    @Test
    public void testListBasicToListBasic1() {
        conversionProvider.registerConverter(new ListBasicToListBasicConverter(conversionProvider));
        boolean canConvert = conversionProvider.canConvert(
                parameterizedOf(List.class, Book.class),
                parameterizedOf(List.class, Dog.class));
        log.info("Book to Dog: {}", canConvert);
        canConvert = conversionProvider.canConvert(
                parameterizedOf(List.class, String.class),
                parameterizedOf(List.class, Integer.class));
        log.info("String to Integer: {}", canConvert);
        canConvert = conversionProvider.canConvert(
                parameterizedOf(List.class, Cat.class),
                parameterizedOf(List.class, Dog.class));
        log.info("Cat to Dog: {}", canConvert);
        conversionProvider.registerConverter(new CatToDogConverter(conversionProvider));
        canConvert = conversionProvider.canConvert(
                parameterizedOf(List.class, Cat.class),
                parameterizedOf(List.class, Dog.class));
        log.info("Cat to Dog: {}", canConvert);
    }

}
