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
