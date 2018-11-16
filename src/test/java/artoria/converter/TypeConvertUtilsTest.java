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
    public void test1() {
        int n = 102;
        Object o = TypeConvertUtils.convert(n, double.class);
        Double d = (Double) o;
        log.info("" + d);
    }

    @Test
    public void test2() {
        String n = "102";
        Object o = TypeConvertUtils.convert(n, double.class);
        Double d = (Double) o;
        log.info("" + d);
    }

    @Test
    public void test3() {
        String n = "true";
        Object o = TypeConvertUtils.convert(n, Boolean.class);
        log.info("" + o);
    }

    @Test
    public void test4() {
        Object o = TypeConvertUtils.convert(true, String.class);
        log.info("" + o);
    }

    @Test
    public void test5() {
        Object o = TypeConvertUtils.convert(new Date(), Timestamp.class);
        log.info("" + o.getClass());
        log.info("" + o);
    }

    @Test
    public void test6() {
        Object o = TypeConvertUtils.convert(DateUtils.format(), java.sql.Date.class);
        log.info("" + o.getClass());
        log.info("" + o);
    }

    @Test
    public void test7() {
        Object o = TypeConvertUtils.convert(DateUtils.getTimestamp() + "", java.sql.Date.class);
        log.info("" + o.getClass());
        log.info("" + o);
        Object o1 = TypeConvertUtils.convert("-45674576567", java.sql.Date.class);
        log.info("" + o1.getClass());
        log.info("" + o1);
    }

}
