package artoria.converter;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.time.DateUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

public class TypeConvertUtilsTest {
    private static Logger log = LoggerFactory.getLogger(TypeConvertUtilsTest.class);

    @Test
    public void testIntToPrimitiveDouble() {
        int src = 102;
        Object obj = TypeConvertUtils.convert(src, double.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToPrimitiveDouble() {
        String src = "102";
        Object obj = TypeConvertUtils.convert(src, double.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToBoolean() {
        String src = "true";
        Object obj = TypeConvertUtils.convert(src, Boolean.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testPrimitiveBooleanToString() {
        Object obj = TypeConvertUtils.convert(true, String.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testDateToTimestamp() {
        Object obj = TypeConvertUtils.convert(new Date(), Timestamp.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testStringToDate() {
        Object obj;
        obj = TypeConvertUtils.convert("2019-03-25 10:10:10 300", java.sql.Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = TypeConvertUtils.convert("2019-03-25T10:10:10.300+0800", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = TypeConvertUtils.convert("2019-03-25 10:10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = TypeConvertUtils.convert("2019-03-25 10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = TypeConvertUtils.convert("2019-03-25", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = TypeConvertUtils.convert("2019/03/25 10:10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = TypeConvertUtils.convert("2019/03/25 10:10", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = TypeConvertUtils.convert("2019/03/25", Date.class);
        log.info("{} {}", obj.getClass(), DateUtils.format((Date) obj));
        obj = TypeConvertUtils.convert("test2019/03/25error", Date.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testLongStringToDate() {
        Object obj = TypeConvertUtils.convert(String.valueOf(DateUtils.getTimestamp()), java.sql.Date.class);
        log.info("{} {}", obj.getClass(), obj);
        Object obj1 = TypeConvertUtils.convert("-45674576567", java.sql.Date.class);
        log.info("{} {}", obj1.getClass(), obj1);
    }

}
