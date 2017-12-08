package artoriatest.util;

import org.junit.Test;
import artoria.util.Reflect;
import artoria.util.Time;

import java.util.Calendar;

public class ReflectTest {

    private static final String TEST_VAL = "hello, world! ";
    private final String TEST_VAL1 = "hello, world! ";

    @Test
    public void test1() throws Exception {
        Reflect reflect = Reflect.create(Time.class);
        Object call = reflect.call("on");
        System.out.println(call);
        Object calendar = reflect.setBean(call).get("calendar");
        System.out.println(calendar instanceof Calendar);
        System.out.println(Time.create(((Calendar) calendar).getTime()));
    }

    @Test
    public void test2() throws Exception {
        Reflect reflect = Reflect.create(Time.class);
        System.out.println(reflect.setBean(reflect.call("on")).call("format"));
        Time time = Time.create(1990, 1, 1, 12, 12, 12);
        System.out.println(reflect.set("calendar", time.getCalendar()).call("format"));
    }

    @Test
    public void test3() throws Exception {
        Reflect reflect = Reflect.create(ReflectTest.class).create();
        System.out.println(reflect.get("TEST_VAL"));
        reflect.set("TEST_VAL", "123456");
        System.out.println(reflect.get("TEST_VAL"));
    }

    @Test
    public void test4() throws Exception {
        Reflect reflect = Reflect.create(ReflectTest.class).create();
        System.out.println(reflect.get("TEST_VAL1"));
        reflect.set("TEST_VAL1", "123456");
        System.out.println(reflect.get("TEST_VAL1"));
        System.out.println(reflect.get("TEST_VAL"));
    }

}
