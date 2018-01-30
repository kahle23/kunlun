package com.apyhs.artoria.time;

import com.apyhs.artoria.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date tools.
 * @author Kahle
 */
public class DateUtils {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

    public static boolean equals(Date date1, Date date2) {
        return date1 == null ? date2 == null : date2 != null && (date1 == date2 || date1.equals(date2));
    }

    public static boolean equals(Calendar cal1, Calendar cal2) {
        return cal1 == null ? cal2 == null : cal2 != null && (cal1 == cal2 || cal1.equals(cal2));
    }

    public static long getUnixTimestamp() {
        long millis = System.currentTimeMillis();
        return millis / 1000;
    }

    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    public static Date parse(Long timestamp) {
        timestamp = timestamp != null ? timestamp : 0;
        return new Date(timestamp);
    }

    public static Date parse(String dateString) throws ParseException {
        return DateUtils.parse(dateString, DEFAULT_DATE_PATTERN);
    }

    public static Date parse(String dateString, String pattern) throws ParseException {
        Assert.notBlank(pattern, "Pattern must is not blank. ");
        Assert.notBlank(dateString, "Date string must is not blank. ");
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(dateString);
    }

    public static String format() {
        return DateUtils.format(new Date());
    }

    public static String format(Long timestamp) {
        return DateUtils.format(timestamp, DEFAULT_DATE_PATTERN);
    }

    public static String format(String pattern) {
        return DateUtils.format(new Date(), pattern);
    }

    public static String format(Date date) {
        return DateUtils.format(date, DEFAULT_DATE_PATTERN);
    }

    public static String format(Long timestamp, String pattern) {
        Date date = DateUtils.parse(timestamp);
        return DateUtils.format(date, pattern);
    }

    public static String format(Date date, String pattern) {
        Assert.notNull(date, "Date must is not null. ");
        Assert.notBlank(pattern, "Pattern must is not blank. ");
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

}
