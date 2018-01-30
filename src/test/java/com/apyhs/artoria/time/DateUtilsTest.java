package com.apyhs.artoria.time;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void testEquals() {
        System.out.println();

        Date date1 = DateTime.create(1000, 1, 1).getDate();
        Date date2 = DateTime.create(1000, 1, 1).getDate();
        System.out.println("date1 equals date2 is true ? result : " + DateUtils.equals(date1, date2));

        Calendar calendar1 = DateTime.create(2000, 1, 1).getCalendar();
        Calendar calendar2 = DateTime.create(2000, 1, 1).getCalendar();
        System.out.println("calendar1 equals calendar2 is true ? result : " + DateUtils.equals(calendar1, calendar2));

        System.out.println();
    }

    @Test
    public void testFormat() {
        System.out.println();

        String pattern = "yyyy-MM-dd'T'HH:mm:ss.Z";
        System.out.println("Test format, The date will new, The pattern is \"" + pattern + "\". ");
        System.out.println("The method format(), result : " + DateUtils.format());
        System.out.println("The method format(Long), result : " + DateUtils.format(new Date().getTime()));
        System.out.println("The method format(String), result : " + DateUtils.format(pattern));
        System.out.println("The method format(Date), result : " + DateUtils.format(new Date()));
        System.out.println("The method format(Long, String), result : " + DateUtils.format(new Date().getTime(), pattern));
        System.out.println("The method format(Date, String), result : " + DateUtils.format(new Date(), pattern));

        System.out.println();
    }

    @Test
    public void testParseFormat() throws ParseException {
        System.out.println();

        System.out.println("The method parse(Long), result : " + DateUtils.parse(DateTime.create().getTimeInMillis()));
        System.out.println("The method parse(String), result : " + DateUtils.parse(DateTime.create().toString()));
        System.out.println("The method parse(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result : "
                + DateUtils.format(DateUtils.parse(DateTime.create().toString(), "yyyy-MM-dd HH:mm:ss SSS")));

        System.out.println();
    }

    @Test
    public void testTimestamp() {
        System.out.println();

        System.out.println("The method getTimestamp(), result : " + DateUtils.getTimestamp());
        System.out.println("The method getUnixTimestamp(), result : " + DateUtils.getUnixTimestamp());

        System.out.println();
    }

}
