package sabertest.util;

import org.junit.Test;
import saber.util.Time;

import java.util.Date;

public class TimeTest {

    @Test
    public void test1() {
        Date d1 = Time.on().setYear(1000).setMonth(1).setDay(1).getDate();
        Date d2 = Time.on(1970, 1, 1).getDate();
        System.out.println(d1.equals(new Date()));
        System.out.println(d1.after(new Date()));
        System.out.println(new Date().after(d1));
        System.out.println(d1.after(d2));
        System.out.println(d2.after(d1));
    }

    @Test
    public void test2() {
        Date date = Time.on(2038, 1, 1).getDate();
        System.out.println(date.getTime() / 1000);
        System.out.println(Integer.MAX_VALUE);
    }

    @Test
    public void test3() {
        System.out.println(Long.MAX_VALUE);
        System.out.println(Time.on().getTimestamp());
    }

}
