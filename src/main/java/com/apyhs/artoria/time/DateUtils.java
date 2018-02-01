package com.apyhs.artoria.time;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date tools.
 * @author Kahle
 * // TODO: Optimize DateTime and SimpleDateFormater and SimpleDateParser by ThreadLocal
 */
public class DateUtils {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static Logger log = LoggerFactory.getLogger(DateUtils.class);
    private static DateFormater dateFormater;
    private static DateParser dateParser;

    static {
        DateUtils.setDateFormater(new SimpleDateFormater());
        DateUtils.setDateParser(new SimpleDateParser());
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
        return DateTime.create(date).addYear(addYear).getDate();
    }

    public static Date addMonth(Date date, int addMonth) {
        return DateTime.create(date).addMonth(addMonth).getDate();
    }

    public static Date addDay(Date date, int addDay) {
        return DateTime.create(date).addDay(addDay).getDate();
    }

    public static Date addHour(Date date, int addHour) {
        return DateTime.create(date).addHour(addHour).getDate();
    }

    public static Date addMinute(Date date, int addMinute) {
        return DateTime.create(date).addMinute(addMinute).getDate();
    }

    public static Date addSecond(Date date, int addSecond) {
        return DateTime.create(date).addSecond(addSecond).getDate();
    }

    public static Date addMillisecond(Date date, int addMillisecond) {
        return DateTime.create(date).addMillisecond(addMillisecond).getDate();
    }

    public static int getYear(Date date) {
        return DateTime.create(date).getYear();
    }

    public static Date setYear(Date date, int year) {
        return DateTime.create(date).setYear(year).getDate();
    }

    public static int getMonth(Date date) {
        return DateTime.create(date).getMonth();
    }

    public static Date setMonth(Date date, int month) {
        return DateTime.create(date).setMonth(month).getDate();
    }

    public static int getDay(Date date) {
        return DateTime.create(date).getDay();
    }

    public static Date setDay(Date date, int day) {
        return DateTime.create(date).setDay(day).getDate();
    }

    public static int getHour(Date date) {
        return DateTime.create(date).getHour();
    }

    public static Date setHour(Date date, int hour) {
        return DateTime.create(date).setHour(hour).getDate();
    }

    public static int getMinute(Date date) {
        return DateTime.create(date).getMinute();
    }

    public static Date setMinute(Date date, int minute) {
        return DateTime.create(date).setMinute(minute).getDate();
    }

    public static int getSecond(Date date) {
        return DateTime.create(date).getSecond();
    }

    public static Date setSecond(Date date, int second) {
        return DateTime.create(date).setSecond(second).getDate();
    }

    public static int getMillisecond(Date date) {
        return DateTime.create(date).getMillisecond();
    }

    public static Date setMillisecond(Date date, int millisecond) {
        return DateTime.create(date).setMillisecond(millisecond).getDate();
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

    private static class SimpleDateParser implements DateParser {

        @Override
        public Date parse(String dateString, String pattern) throws ParseException {
            Assert.notBlank(dateString, "Parameter \"dateString\" must not blank. ");
            Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(dateString);
        }

    }

    private static class SimpleDateFormater implements DateFormater {

        @Override
        public String format(Date date, String pattern) {
            Assert.notNull(date, "Parameter \"date\" must not null. ");
            Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        }

    }

}
