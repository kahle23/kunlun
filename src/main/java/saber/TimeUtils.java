package saber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class TimeUtils {
    private static final String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

    public static boolean equal(Date d1, Date d2) {
        return d1 == null ? d2 == null : d2 != null && (d1 == d2 || d1.equals(d2));
    }

    public static long unixTimestamp() {
        return new Date().getTime() / 1000;
    }

    public static long unixTimestamp(Date date) {
        return date.getTime() / 1000;
    }

    public static Date parseUnixTimestamp(long timestamp) {
        return new Date(timestamp * 1000L);
    }

    public static long timestamp() {
        return new Date().getTime();
    }

    public static long timestamp(Date date) {
        return date.getTime();
    }

    public static Date parseTimestamp(long timestamp) {
        return new Date(timestamp);
    }

    public static String format() {
        return new SimpleDateFormat(DEFAULT_TIME_PATTERN).format(new Date());
    }

    public static String format(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String format(long timestamp) {
        return new SimpleDateFormat(DEFAULT_TIME_PATTERN).format(new Date(timestamp));
    }

    public static String format(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(timestamp));
    }

    public static String format(Date date) {
        return new SimpleDateFormat(DEFAULT_TIME_PATTERN).format(date);
    }

    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date parse(String source) throws ParseException {
        return new SimpleDateFormat(DEFAULT_TIME_PATTERN).parse(source);
    }

    public static Date parse(String source, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(source);
    }

    public static Time time() {
        return new Time();
    }

    public static Time time(Date date) {
        return new Time(date);
    }

    public static Time time(Calendar calendar) {
        return new Time(calendar);
    }

    public static Time timeByYmd(int year, int month, int day) {
        return new Time().setMillisecond(0).setSecond(0)
                .setMinute(0).setHour(0).setDay(day).setMonth(month).setYear(year);
    }

    public static Time timeByYmdHmss(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        return new Time().setMillisecond(millisecond).setSecond(second)
                .setMinute(minute).setHour(hour).setDay(day).setMonth(month).setYear(year);
    }

    public static Time timeInDayStart(Date date) {
        return new Time(date).setMillisecond(0).setSecond(0).setMinute(0).setHour(0);
    }

    public static Time timeInDayEnd(Date date) {
        return new Time(date).setMillisecond(0).setSecond(0).setMinute(0).setHour(24);
    }

    public static Time timeInMonthStart(Date date) {
        return new Time(date).setMillisecond(0).setSecond(0).setMinute(0).setHour(0).setDay(1);
    }

    public static Time timeInMonthEnd(Date date) {
        return new Time(date).setMillisecond(0).setSecond(0).setMinute(0).setHour(0).setDay(1).addMonth(1);
    }

}
