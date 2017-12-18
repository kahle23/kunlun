package artoriatest.util;

import artoria.util.DateUtils;
import artoria.util.ReflectUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class ReflectUtilsTest {

    private static final String TEST_VAL = "hello, world! ";
    private final String TEST_VAL1 = "hello, world! ";

    @Test
    public void test_toString() throws Exception {
        System.out.println(ReflectUtils.create(DateUtils.class));
        System.out.println(ReflectUtils.create(ReflectUtils.class));
    }

    @Test
    public void test1() throws Exception {
        ReflectUtils reflect = ReflectUtils.create(DateUtils.class);
        System.out.println(reflect);
        Object call = reflect.call("create", "2010-10-10 10:10:10 010");
        System.out.println(call);
        Object calendar = reflect.setBean(call).get("calendar");
        System.out.println(calendar instanceof Calendar);
        System.out.println(DateUtils.create(((Calendar) calendar).getTime()));
    }

    @Test
    public void test2() throws Exception {
        ReflectUtils reflect = ReflectUtils.create(DateUtils.class);
        System.out.println(reflect.setBean(reflect.call("create")).call("format"));
        DateUtils time = DateUtils.create(1990, 1, 1, 12, 12, 12);
        System.out.println(reflect.set("calendar", time.getCalendar()).call("format"));
    }

    @Test
    public void test3() throws Exception {
        ReflectUtils reflect = ReflectUtils.create(ReflectUtilsTest.class).newInstance();
        System.out.println(reflect.get("TEST_VAL"));
        reflect.set("TEST_VAL", "123456");
        System.out.println(reflect.get("TEST_VAL"));
    }

    @Test
    public void test4() throws Exception {
        ReflectUtils reflect = ReflectUtils.create(ReflectUtilsTest.class).newInstance();
        System.out.println(reflect.get("TEST_VAL1"));
        reflect.set("TEST_VAL1", "123456");
        System.out.println(reflect.get("TEST_VAL1"));
        System.out.println(reflect.get("TEST_VAL"));
    }

}
