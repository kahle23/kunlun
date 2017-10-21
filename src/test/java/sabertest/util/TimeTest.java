package sabertest.util;

import org.junit.Test;
import saber.util.Time;

import java.util.Calendar;
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

    @Test
    public void test4() throws Exception {
        String patten = "yyyy-MM-dd HH:mm:ss SSS";

        Time time = Time.on(1999, 12, 21);
        Object format = time.format();
        Object format1 = time.format(patten);
        Object timestamp = time.getTimestamp();
        Object calendar = time.getCalendar();
        Object date = time.getDate();

        System.out.println(Time.on(calendar));
        System.out.println(Time.on(date));
        System.out.println(Time.on(format));
        System.out.println(Time.on(format1, patten));
        System.out.println(Time.on(timestamp));

        System.out.println(Time.on(format).addHour(8).addDay(5));
    }

}
