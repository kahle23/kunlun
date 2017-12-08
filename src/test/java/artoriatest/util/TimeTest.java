package artoriatest.util;

import org.junit.Test;
import artoria.util.Time;

import java.util.Calendar;
import java.util.Date;

public class TimeTest {

    @Test
    public void testDateAndCalendar() {
        int count = 20;
        int createCount = 100000;

        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < createCount; j++) {
                new Date();
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();

        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < createCount; j++) {
                Calendar.getInstance();
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();
    }

    @Test
    public void test1() {
        Date d1 = Time.create().setYear(1000).setMonth(1).setDay(1).getDate();
        Date d2 = Time.create(1970, 1, 1).getDate();
        System.out.println(d1.equals(new Date()));
        System.out.println(d1.after(new Date()));
        System.out.println(new Date().after(d1));
        System.out.println(d1.after(d2));
        System.out.println(d2.after(d1));
    }

    @Test
    public void test2() {
        Date date = Time.create(2038, 1, 1).getDate();
        System.out.println(date.getTime() / 1000);
        System.out.println(Integer.MAX_VALUE);
    }

    @Test
    public void test3() {
        System.out.println(Long.MAX_VALUE);
        System.out.println(Time.create().getTimestamp());
    }

    @Test
    public void test4() throws Exception {
        String patten = "yyyy-MM-dd HH:mm:ss SSS";

        Time time = Time.create(1999, 12, 21);
        Object format = time.format();
        Object format1 = time.format(patten);
        Object timestamp = time.getTimestamp();
        Object calendar = time.getCalendar();
        Object date = time.getDate();

        System.out.println(Time.create(calendar));
        System.out.println(Time.create(date));
        System.out.println(Time.create(format));
        System.out.println(Time.create(format1, patten));
        System.out.println(Time.create(timestamp));

        System.out.println(Time.create(format).addHour(8).addDay(5));
    }

}
