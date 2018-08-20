package artoria.converter;

import artoria.time.DateUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

public class TypeConvertUtilsTest {

    @Test
    public void test1() {
        int n = 102;
        Object o = TypeConvertUtils.convert(n, double.class);
        Double d = (Double) o;
        System.out.println(d);
    }

    @Test
    public void test2() {
        String n = "102";
        Object o = TypeConvertUtils.convert(n, double.class);
        Double d = (Double) o;
        System.out.println(d);
    }

    @Test
    public void test3() {
        String n = "true";
        Object o = TypeConvertUtils.convert(n, Boolean.class);
        System.out.println(o);
    }

    @Test
    public void test4() {
        Object o = TypeConvertUtils.convert(true, String.class);
        System.out.println(o);
    }

    @Test
    public void test5() {
        Object o = TypeConvertUtils.convert(new Date(), Timestamp.class);
        System.out.println(o.getClass());
        System.out.println(o);
    }

    @Test
    public void test6() {
        Object o = TypeConvertUtils.convert(DateUtils.format(), java.sql.Date.class);
        System.out.println(o.getClass());
        System.out.println(o);
    }

    @Test
    public void test7() {
        Object o = TypeConvertUtils.convert(DateUtils.getTimestamp() + "", java.sql.Date.class);
        System.out.println(o.getClass());
        System.out.println(o);
        Object o1 = TypeConvertUtils.convert("-45674576567", java.sql.Date.class);
        System.out.println(o1.getClass());
        System.out.println(o1);
    }

}
