/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.time;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.ObjUtil;

import java.util.Calendar;
import java.util.Date;

import static kunlun.common.constant.Numbers.*;

/**
 * The date tools.
 * @author Kahle
 */
public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    private static volatile DateTimeFactory dateTimeFactory;
    private static volatile DateProvider dateProvider;
    private static volatile SimpleClock clock;

    public static DateTimeFactory getDateTimeFactory() {
        if (dateTimeFactory != null) { return dateTimeFactory; }
        synchronized (DateUtil.class) {
            if (dateTimeFactory != null) { return dateTimeFactory; }
            DateUtil.setDateTimeFactory(new SimpleDateTimeFactory());
            return dateTimeFactory;
        }
    }

    public static void setDateTimeFactory(DateTimeFactory dateTimeFactory) {
        Assert.notNull(dateTimeFactory, "Parameter \"dateTimeFactory\" must not null. ");
        log.info("Set date time factory: {}", dateTimeFactory.getClass().getName());
        DateUtil.dateTimeFactory = dateTimeFactory;
    }

    public static DateProvider getDateProvider() {
        if (dateProvider != null) { return dateProvider; }
        synchronized (DateUtil.class) {
            if (dateProvider != null) { return dateProvider; }
            DateUtil.setDateProvider(new SimpleDateProvider());
            return dateProvider;
        }
    }

    public static void setDateProvider(DateProvider dateProvider) {
        Assert.notNull(dateProvider, "Parameter \"dateProvider\" must not null. ");
        log.info("Set date provider: {}", dateProvider.getClass().getName());
        DateUtil.dateProvider = dateProvider;
    }

    public static SimpleClock getClock() {
        if (clock != null) { return clock; }
        synchronized (DateUtil.class) {
            if (clock != null) { return clock; }
            DateUtil.setClock(new SimpleClock());
            return clock;
        }
    }

    public static void setClock(SimpleClock clock) {
        Assert.notNull(clock, "Parameter \"clock\" must not null. ");
        log.info("Set clock: {}", clock.getClass().getName());
        DateUtil.clock = clock;
    }

    public static void register(String pattern) {
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        getDateProvider().register(pattern);
    }

    public static void deregister(String pattern) {
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        getDateProvider().deregister(pattern);
    }

    public static DateTime create() {

        return getDateTimeFactory().getInstance();
    }

    public static DateTime create(Long timeInMillis) {

        return getDateTimeFactory().getInstance(timeInMillis);
    }

    public static DateTime create(Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        DateTime dateTime = DateUtil.create();
        return dateTime.setDate(date);
    }

    public static DateTime create(Calendar calendar) {
        Assert.notNull(calendar, "Parameter \"calendar\" must not null. ");
        DateTime dateTime = DateUtil.create();
        return dateTime.setCalendar(calendar);
    }

    public static DateTime create(String dateString) {
        Date date = DateUtil.parse(dateString);
        DateTime dateTime = DateUtil.create();
        return dateTime.setDate(date);
    }

    public static DateTime create(String dateString, String pattern) {
        DateTime dateTime = DateUtil.create();
        Date date = DateUtil.parse(dateString, pattern);
        return dateTime.setDate(date);
    }

    public static DateTime create(int year, int month, int day) {
        DateTime dateTime = DateUtil.create();
        return dateTime.setYear(year).setMonth(month)
                .setDay(day).setHour(ZERO).setMinute(ZERO).setSecond(ZERO).setMillisecond(ZERO);
    }

    public static DateTime create(int year, int month, int day, int hour, int minute, int second) {
        DateTime dateTime = DateUtil.create();
        return dateTime.setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(ZERO);
    }

    public static DateTime create(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        DateTime dateTime = DateUtil.create();
        return dateTime.setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(millisecond);
    }

    public static boolean equals(Date date1, Date date2) {

        return ObjUtil.equals(date1, date2);
    }

    public static boolean equals(Calendar calendar1, Calendar calendar2) {

        return ObjUtil.equals(calendar1, calendar2);
    }

    public static boolean equals(DateTime dateTime1, DateTime dateTime2) {

        return ObjUtil.equals(dateTime1, dateTime2);
    }

    public static long getTimeInMillis() {

        return getClock().getTime();
    }

    public static long getTimeInSeconds() {

        return getClock().getTime() / ONE_THOUSAND;
    }

    public static DateTime getDayOfStart(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtil.create(dateTime.getTimeInMillis());
        result.setHour(ZERO).setMinute(ZERO).setSecond(ZERO).setMillisecond(ZERO);
        return result;
    }

    public static DateTime getDayOfEnd(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtil.create(dateTime.getTimeInMillis());
        result.setSecond(FIFTY_NINE).setMillisecond(NINE_HUNDRED_NINETY_NINE);
        result.setHour(TWENTY_THREE).setMinute(FIFTY_NINE);
        return result;
    }

    public static DateTime getMonthOfStart(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtil.getDayOfStart(dateTime);
        result.setDay(ONE);
        return result;
    }

    public static DateTime getMonthOfEnd(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtil.getDayOfStart(dateTime);
        result.setDay(ONE).addMonth(ONE).addMillisecond(MINUS_ONE);
        return result;
    }

    public static DateTime getYearOfStart(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtil.getMonthOfStart(dateTime);
        result.setMonth(ONE);
        return result;
    }

    public static DateTime getYearOfEnd(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtil.getMonthOfStart(dateTime);
        result.setMonth(ONE).addYear(ONE).addMillisecond(MINUS_ONE);
        return result;
    }

    public static DateTime getWeekOfStart(DateTime dateTime, int firstDayNum) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        Assert.state(firstDayNum >= ONE && firstDayNum <= SEVEN
                , "Parameter \"firstDayNum\" must >= 1 and <= 7. ");
        DateTime result = DateUtil.getDayOfStart(dateTime);
        // Default first day of week is sunday and value is "1".
        int dayOfWeek = result.getDayOfWeek();
        dayOfWeek = dayOfWeek == ONE ? SEVEN : dayOfWeek - ONE;
        if (ONE <= dayOfWeek && dayOfWeek < firstDayNum) {
            result.addDay(firstDayNum - SEVEN - dayOfWeek);
        }
        else {
            result.addDay(firstDayNum - dayOfWeek);
        }
        return result;
    }

    public static DateTime getWeekOfEnd(DateTime dateTime, int firstDayNum) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtil.getWeekOfStart(dateTime, firstDayNum);
        result.addDay(SEVEN).addMillisecond(MINUS_ONE);
        return result;
    }

    public static Date addYear(Date date, int addYear) {

        return DateUtil.create(date).addYear(addYear).getDate();
    }

    public static Date addMonth(Date date, int addMonth) {

        return DateUtil.create(date).addMonth(addMonth).getDate();
    }

    public static Date addDay(Date date, int addDay) {

        return DateUtil.create(date).addDay(addDay).getDate();
    }

    public static Date addHour(Date date, int addHour) {

        return DateUtil.create(date).addHour(addHour).getDate();
    }

    public static Date addMinute(Date date, int addMinute) {

        return DateUtil.create(date).addMinute(addMinute).getDate();
    }

    public static Date addSecond(Date date, int addSecond) {

        return DateUtil.create(date).addSecond(addSecond).getDate();
    }

    public static Date addMillisecond(Date date, int addMillisecond) {

        return DateUtil.create(date).addMillisecond(addMillisecond).getDate();
    }

    public static int getYear(Date date) {

        return DateUtil.create(date).getYear();
    }

    public static Date setYear(Date date, int year) {

        return DateUtil.create(date).setYear(year).getDate();
    }

    public static int getMonth(Date date) {

        return DateUtil.create(date).getMonth();
    }

    public static Date setMonth(Date date, int month) {

        return DateUtil.create(date).setMonth(month).getDate();
    }

    public static int getDay(Date date) {

        return DateUtil.create(date).getDay();
    }

    public static Date setDay(Date date, int day) {

        return DateUtil.create(date).setDay(day).getDate();
    }

    public static int getHour(Date date) {

        return DateUtil.create(date).getHour();
    }

    public static Date setHour(Date date, int hour) {

        return DateUtil.create(date).setHour(hour).getDate();
    }

    public static int getMinute(Date date) {

        return DateUtil.create(date).getMinute();
    }

    public static Date setMinute(Date date, int minute) {

        return DateUtil.create(date).setMinute(minute).getDate();
    }

    public static int getSecond(Date date) {

        return DateUtil.create(date).getSecond();
    }

    public static Date setSecond(Date date, int second) {

        return DateUtil.create(date).setSecond(second).getDate();
    }

    public static int getMillisecond(Date date) {

        return DateUtil.create(date).getMillisecond();
    }

    public static Date setMillisecond(Date date, int millisecond) {

        return DateUtil.create(date).setMillisecond(millisecond).getDate();
    }

    public static Date parse(Long timestamp) {
        timestamp = timestamp != null ? timestamp : ZERO;
        return new Date(timestamp);
    }

    public static Date parse(String dateString) {

        return getDateProvider().parse(dateString);
    }

    public static Date parse(String dateString, String pattern) {

        return getDateProvider().parse(dateString, pattern);
    }

    public static String format() {

        return DateUtil.format(new Date());
    }

    public static String format(String pattern) {

        return DateUtil.format(new Date(), pattern);
    }

    public static String format(Date date) {

        return getDateProvider().format(date);
    }

    public static String format(Long timestamp) {

        return DateUtil.format(DateUtil.parse(timestamp));
    }

    public static String format(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        return DateUtil.format(dateTime.getDate());
    }

    public static String format(Date date, String pattern) {

        return getDateProvider().format(date, pattern);
    }

    public static String format(Long timestamp, String pattern) {
        Date date = DateUtil.parse(timestamp);
        return DateUtil.format(date, pattern);
    }

    public static String format(DateTime dateTime, String pattern) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        return DateUtil.format(dateTime.getDate(), pattern);
    }

}
