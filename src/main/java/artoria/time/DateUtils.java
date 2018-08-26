package artoria.time;

import artoria.exception.ExceptionUtils;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Date tools.
 * @author Kahle
 */
public class DateUtils {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    private static Logger log = Logger.getLogger(DateUtils.class.getName());
    private static Class<? extends DateTime> dateTimeClass;
    private static DateFormater dateFormater;
    private static DateParser dateParser;

    static {
        SimpleDateHandler handler = new SimpleDateHandler();
        DateUtils.setDateFormater(handler);
        DateUtils.setDateParser(handler);
        DateUtils.setDateTimeClass(SimpleDateTime.class);
    }

    public static Class<? extends DateTime> getDateTimeClass() {
        return dateTimeClass;
    }

    public static void setDateTimeClass(Class<? extends DateTime> dateTimeClass) {
        Assert.notNull(dateTimeClass, "Parameter \"dateTimeClass\" must not null. ");
        log.info("Set dateTime class: " + dateTimeClass.getName());
        DateUtils.dateTimeClass = dateTimeClass;
    }

    public static DateFormater getDateFormater() {
        return dateFormater;
    }

    public static void setDateFormater(DateFormater formater) {
        Assert.notNull(formater, "Parameter \"formater\" must not null. ");
        log.info("Set date formater: " + formater.getClass().getName());
        dateFormater = formater;
    }

    public static DateParser getDateParser() {
        return dateParser;
    }

    public static void setDateParser(DateParser parser) {
        Assert.notNull(parser, "Parameter \"parser\" must not null. ");
        log.info("Set date parser: " + parser.getClass().getName());
        dateParser = parser;
    }

    public static DateTime create() {
        try {
            return dateTimeClass.newInstance();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static DateTime create(Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        DateTime dateTime = DateUtils.create();
        return dateTime.setDate(date);
    }

    public static DateTime create(Calendar calendar) {
        Assert.notNull(calendar, "Parameter \"calendar\" must not null. ");
        DateTime dateTime = DateUtils.create();
        return dateTime.setCalendar(calendar);
    }

    public static DateTime create(Long timestamp) {
        DateTime dateTime = DateUtils.create();
        timestamp = timestamp != null ? timestamp : 0;
        return dateTime.setTimeInMillis(timestamp);
    }

    public static DateTime create(String dateString) throws ParseException {
        Date date = DateUtils.parse(dateString);
        DateTime dateTime = DateUtils.create();
        return dateTime.setDate(date);
    }

    public static DateTime create(String dateString, String pattern) throws ParseException {
        DateTime dateTime = DateUtils.create();
        Date date = DateUtils.parse(dateString, pattern);
        return dateTime.setDate(date);
    }

    public static DateTime create(int year, int month, int day) {
        DateTime dateTime = DateUtils.create();
        return dateTime.setYear(year).setMonth(month)
                .setDay(day).setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
    }

    public static DateTime create(int year, int month, int day, int hour, int minute, int second) {
        DateTime dateTime = DateUtils.create();
        return dateTime.setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(0);
    }

    public static DateTime create(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        DateTime dateTime = DateUtils.create();
        return dateTime.setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(millisecond);
    }

    public static boolean equals(Date date1, Date date2) {
        return ObjectUtils.equals(date1, date2);
    }

    public static boolean equals(Calendar calendar1, Calendar calendar2) {
        return ObjectUtils.equals(calendar1, calendar2);
    }

    public static boolean equals(DateTime dateTime1, DateTime dateTime2) {
        return ObjectUtils.equals(dateTime1, dateTime2);
    }

    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    public static long getUnixTimestamp() {
        long millis = System.currentTimeMillis();
        return millis / 1000;
    }

    public static Date addYear(Date date, int addYear) {
        return DateUtils.create(date).addYear(addYear).getDate();
    }

    public static Date addMonth(Date date, int addMonth) {
        return DateUtils.create(date).addMonth(addMonth).getDate();
    }

    public static Date addDay(Date date, int addDay) {
        return DateUtils.create(date).addDay(addDay).getDate();
    }

    public static Date addHour(Date date, int addHour) {
        return DateUtils.create(date).addHour(addHour).getDate();
    }

    public static Date addMinute(Date date, int addMinute) {
        return DateUtils.create(date).addMinute(addMinute).getDate();
    }

    public static Date addSecond(Date date, int addSecond) {
        return DateUtils.create(date).addSecond(addSecond).getDate();
    }

    public static Date addMillisecond(Date date, int addMillisecond) {
        return DateUtils.create(date).addMillisecond(addMillisecond).getDate();
    }

    public static int getYear(Date date) {
        return DateUtils.create(date).getYear();
    }

    public static Date setYear(Date date, int year) {
        return DateUtils.create(date).setYear(year).getDate();
    }

    public static int getMonth(Date date) {
        return DateUtils.create(date).getMonth();
    }

    public static Date setMonth(Date date, int month) {
        return DateUtils.create(date).setMonth(month).getDate();
    }

    public static int getDay(Date date) {
        return DateUtils.create(date).getDay();
    }

    public static Date setDay(Date date, int day) {
        return DateUtils.create(date).setDay(day).getDate();
    }

    public static int getHour(Date date) {
        return DateUtils.create(date).getHour();
    }

    public static Date setHour(Date date, int hour) {
        return DateUtils.create(date).setHour(hour).getDate();
    }

    public static int getMinute(Date date) {
        return DateUtils.create(date).getMinute();
    }

    public static Date setMinute(Date date, int minute) {
        return DateUtils.create(date).setMinute(minute).getDate();
    }

    public static int getSecond(Date date) {
        return DateUtils.create(date).getSecond();
    }

    public static Date setSecond(Date date, int second) {
        return DateUtils.create(date).setSecond(second).getDate();
    }

    public static int getMillisecond(Date date) {
        return DateUtils.create(date).getMillisecond();
    }

    public static Date setMillisecond(Date date, int millisecond) {
        return DateUtils.create(date).setMillisecond(millisecond).getDate();
    }

    public static Date parse(Long timestamp) {
        timestamp = timestamp != null ? timestamp : 0;
        return new Date(timestamp);
    }

    public static Date parse(String dateString) throws ParseException {
        return DateUtils.parse(dateString, DEFAULT_DATE_PATTERN);
    }

    public static Date parse(String dateString, String pattern) throws ParseException {
        return dateParser.parse(dateString, pattern);
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
        return dateFormater.format(date, pattern);
    }

}
