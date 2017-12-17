package artoriatest.util;

import org.junit.Test;
import artoria.util.ReflectUtils;
import artoria.util.DateUtils;

import java.util.Calendar;

public class ReflectUtilsTest {

    private static final String TEST_VAL = "hello, world! ";
    private final String TEST_VAL1 = "hello, world! ";

    @Test
    public void test1() throws Exception {
        ReflectUtils reflect = ReflectUtils.create(DateUtils.class);
        Object call = reflect.call("on");
        System.out.println(call);
        Object calendar = reflect.setBean(call).get("calendar");
        System.out.println(calendar instanceof Calendar);
        System.out.println(DateUtils.create(((Calendar) calendar).getTime()));
    }

    @Test
    public void test2() throws Exception {
        ReflectUtils reflect = ReflectUtils.create(DateUtils.class);
        System.out.println(reflect.setBean(reflect.call("on")).call("format"));
        DateUtils time = DateUtils.create(1990, 1, 1, 12, 12, 12);
        System.out.println(reflect.set("calendar", time.getCalendar()).call("format"));
    }

    @Test
    public void test3() throws Exception {
        ReflectUtils reflect = ReflectUtils.create(ReflectUtilsTest.class).construct();
        System.out.println(reflect.get("TEST_VAL"));
        reflect.set("TEST_VAL", "123456");
        System.out.println(reflect.get("TEST_VAL"));
    }

    @Test
    public void test4() throws Exception {
        ReflectUtils reflect = ReflectUtils.create(ReflectUtilsTest.class).construct();
        System.out.println(reflect.get("TEST_VAL1"));
        reflect.set("TEST_VAL1", "123456");
        System.out.println(reflect.get("TEST_VAL1"));
        System.out.println(reflect.get("TEST_VAL"));
    }

}
