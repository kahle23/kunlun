package apyh.artoria.converter;

import apyh.artoria.util.DateUtils;
import apyh.artoria.util.ReflectUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

public class TypeUtilsTest {

    @Test
    public void test1() {
        int n = 102;
        Object o = TypeUtils.convert(n, double.class);
        Double d = (Double) o;
        System.out.println(d);
    }

    @Test
    public void test2() {
        String n = "102";
        Object o = TypeUtils.convert(n, double.class);
        Double d = (Double) o;
        System.out.println(d);
    }

    @Test
    public void test3() {
        String n = "true";
        Object o = TypeUtils.convert(n, Boolean.class);
        System.out.println(o);
    }

    @Test
    public void test4() {
        Object o = TypeUtils.convert(true, String.class);
        System.out.println(o);
    }

    @Test
    public void test5() {
        Object o = TypeUtils.convert(new Date(), Timestamp.class);
        System.out.println(o.getClass());
        System.out.println(o);
    }

    @Test
    public void test6() {
        Object o = TypeUtils.convert(DateUtils.format(), java.sql.Date.class);
        System.out.println(o.getClass());
        System.out.println(o);
    }

}
