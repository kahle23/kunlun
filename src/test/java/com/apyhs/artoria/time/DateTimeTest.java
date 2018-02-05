package com.apyhs.artoria.time;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateTimeTest {

    @Test
    public void ifUnixTimestampUsingInteger() {
        System.out.println();
        System.out.println("If unix timestamp using Integer. ");
        DateTime dateTime = DateTime.create(2038, 1, 23);
        System.out.println("The time of unix timestamp is " + dateTime);
        System.out.println("The unix timestamp is " + dateTime.getTimeInSeconds());
        System.out.println("The Integer max is " + Integer.MAX_VALUE);
        System.out.println();
    }

    @Test
    public void testEquals() {
        System.out.println();

        DateTime dateTime1 = DateTime.create(1991, 11, 12);
        DateTime dateTime2 = DateTime.create(1991, 11, 12);
        System.out.println("dateUtils1 equals dateUtils2 is true ? result : " + dateTime1.equals(dateTime2));

        System.out.println();
    }

    @Test
    public void testFormat() {
        System.out.println();

        System.out.println("The method toString(), result : " + DateTime.create().toString());

        System.out.println();
    }

    @Test
    public void testCreate() throws ParseException {
        System.out.println();

        System.out.println("The method create(), result : " + DateTime.create().toString());
        System.out.println("The method create(Date), result : " + DateTime.create(new Date()).toString());
        System.out.println("The method create(Long), result : " + DateTime.create(new Date().getTime()).toString());
        System.out.println("The method create(Calendar), result : " + DateTime.create(Calendar.getInstance()));
        System.out.println("The method create(String), result : " + DateTime.create(DateTime.create().toString()));
        System.out.println("The method create(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result : "
                + DateTime.create(DateTime.create().toString(), "yyyy-MM-dd HH:mm:ss SSS"));
        System.out.println("The method create(1990, 12, 12), result : " + DateTime.create(1990, 12, 12));
        System.out.println("The method create(1990, 12, 12, 12, 12, 12), result : "
                + DateTime.create(1990, 12, 12, 12, 12, 12));
        System.out.println("The method create(1990, 12, 12, 12, 12, 12, 12), result : "
                + DateTime.create(1990, 12, 12, 12, 12, 12, 12));

        System.out.println();
    }

    @Test
    public void testTimestamp() {
        System.out.println();

        System.out.println("The method create().getTimeInMillis(), result : " + DateTime.create().getTimeInMillis());
        System.out.println("The method create().getTimeInSeconds(), result : " + DateTime.create().getTimeInSeconds());

        System.out.println();
    }

}
