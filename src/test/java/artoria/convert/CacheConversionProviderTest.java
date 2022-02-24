package artoria.convert;

import artoria.cache.Cache;
import artoria.cache.CacheUtils;
import artoria.cache.SimpleCache;
import artoria.convert.support.CatToDogConverter;
import artoria.convert.support.ListBasicToListBasicConverter;
import artoria.convert.support.NumberToDateConverter;
import artoria.lang.ReferenceType;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.mock.MockUtils;
import artoria.test.bean.Book;
import artoria.test.bean.Cat;
import artoria.test.bean.Dog;
import artoria.time.DateUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.ZERO;
import static artoria.util.ObjectUtils.cast;
import static artoria.util.TypeUtils.parameterizedOf;

public class CacheConversionProviderTest {
    private static Logger log = LoggerFactory.getLogger(CacheConversionProviderTest.class);
    private static ConversionProvider conversionProvider;

    static {
        String cacheName="test";
        Cache cache = new SimpleCache(
                cacheName, ZERO, 3 * 60 * 1000, ReferenceType.SOFT);
        CacheUtils.register(cache);
        conversionProvider = new SimpleConversionProvider();
        conversionProvider = new CacheConversionProvider(
                conversionProvider, cacheName, 3L, TimeUnit.MINUTES);
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
        conversionProvider.addConverter(numberToDateConverter);
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
        conversionProvider.addConverter(new CatToDogConverter(conversionProvider));
        Cat cat = MockUtils.mock(Cat.class);
        log.info("{}", JSON.toJSONString(cat));
        Dog dog = cast(conversionProvider.convert(cat, Dog.class));
        log.info("{}", JSON.toJSONString(dog));
    }

    @Test
    public void testAddAndRemoveConverter() {
        GenericConverter converter1 = new ListBasicToListBasicConverter(conversionProvider);
        GenericConverter converter2 = new CatToDogConverter(conversionProvider);
        conversionProvider.addConverter(converter1);
        conversionProvider.addConverter(converter2);
        conversionProvider.removeConverter(converter1);
        conversionProvider.removeConverter(converter2);
    }

    @Test
    public void testListBasicToListBasic() {
        conversionProvider.addConverter(new ListBasicToListBasicConverter(conversionProvider));
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
        conversionProvider.addConverter(new ListBasicToListBasicConverter(conversionProvider));
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
        conversionProvider.addConverter(new CatToDogConverter(conversionProvider));
        canConvert = conversionProvider.canConvert(
                parameterizedOf(List.class, Cat.class),
                parameterizedOf(List.class, Dog.class));
        log.info("Cat to Dog: {}", canConvert);
    }

}
