package artoria.time;

import artoria.lang.Clock;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.Calendar;
import java.util.Date;

import static artoria.common.Constants.*;

/**
 * The date tools.
 * @author Kahle
 */
public class DateUtils {
    private static Logger log = LoggerFactory.getLogger(DateUtils.class);
    private static volatile DateTimeFactory dateTimeFactory;
    private static volatile DateProvider dateProvider;
    private static volatile Clock clock;

    public static DateTimeFactory getDateTimeFactory() {
        if (dateTimeFactory != null) { return dateTimeFactory; }
        synchronized (DateUtils.class) {
            if (dateTimeFactory != null) { return dateTimeFactory; }
            DateUtils.setDateTimeFactory(new SimpleDateTimeFactory());
            return dateTimeFactory;
        }
    }

    public static void setDateTimeFactory(DateTimeFactory dateTimeFactory) {
        Assert.notNull(dateTimeFactory, "Parameter \"dateTimeFactory\" must not null. ");
        log.info("Set date time factory: {}", dateTimeFactory.getClass().getName());
        DateUtils.dateTimeFactory = dateTimeFactory;
    }

    public static DateProvider getDateProvider() {
        if (dateProvider != null) { return dateProvider; }
        synchronized (DateUtils.class) {
            if (dateProvider != null) { return dateProvider; }
            DateUtils.setDateProvider(new SimpleDateProvider());
            return dateProvider;
        }
    }

    public static void setDateProvider(DateProvider dateProvider) {
        Assert.notNull(dateProvider, "Parameter \"dateProvider\" must not null. ");
        log.info("Set date provider: {}", dateProvider.getClass().getName());
        DateUtils.dateProvider = dateProvider;
    }

    public static Clock getClock() {
        if (clock != null) { return clock; }
        synchronized (DateUtils.class) {
            if (clock != null) { return clock; }
            DateUtils.setClock(new SimpleClock());
            return clock;
        }
    }

    public static void setClock(Clock clock) {
        Assert.notNull(clock, "Parameter \"clock\" must not null. ");
        log.info("Set clock: {}", clock.getClass().getName());
        DateUtils.clock = clock;
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
        DateTime dateTime = DateUtils.create();
        return dateTime.setDate(date);
    }

    public static DateTime create(Calendar calendar) {
        Assert.notNull(calendar, "Parameter \"calendar\" must not null. ");
        DateTime dateTime = DateUtils.create();
        return dateTime.setCalendar(calendar);
    }

    public static DateTime create(String dateString) {
        Date date = DateUtils.parse(dateString);
        DateTime dateTime = DateUtils.create();
        return dateTime.setDate(date);
    }

    public static DateTime create(String dateString, String pattern) {
        DateTime dateTime = DateUtils.create();
        Date date = DateUtils.parse(dateString, pattern);
        return dateTime.setDate(date);
    }

    public static DateTime create(int year, int month, int day) {
        DateTime dateTime = DateUtils.create();
        return dateTime.setYear(year).setMonth(month)
                .setDay(day).setHour(ZERO).setMinute(ZERO).setSecond(ZERO).setMillisecond(ZERO);
    }

    public static DateTime create(int year, int month, int day, int hour, int minute, int second) {
        DateTime dateTime = DateUtils.create();
        return dateTime.setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(ZERO);
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

    public static long getTimeInMillis() {

        return getClock().getTimeInMillis();
    }

    public static long getTimeInSeconds() {

        return getClock().getTimeInMillis() / ONE_THOUSAND;
    }

    public static DateTime getDayOfStart(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtils.create(dateTime.getTimeInMillis());
        result.setHour(ZERO).setMinute(ZERO).setSecond(ZERO).setMillisecond(ZERO);
        return result;
    }

    public static DateTime getDayOfEnd(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtils.create(dateTime.getTimeInMillis());
        result.setSecond(FIFTY_NINE).setMillisecond(NINE_HUNDRED_NINETY_NINE);
        result.setHour(TWENTY_THREE).setMinute(FIFTY_NINE);
        return result;
    }

    public static DateTime getMonthOfStart(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtils.getDayOfStart(dateTime);
        result.setDay(ONE);
        return result;
    }

    public static DateTime getMonthOfEnd(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtils.getDayOfStart(dateTime);
        result.setDay(ONE).addMonth(ONE).addMillisecond(MINUS_ONE);
        return result;
    }

    public static DateTime getYearOfStart(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtils.getMonthOfStart(dateTime);
        result.setMonth(ONE);
        return result;
    }

    public static DateTime getYearOfEnd(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        DateTime result = DateUtils.getMonthOfStart(dateTime);
        result.setMonth(ONE).addYear(ONE).addMillisecond(MINUS_ONE);
        return result;
    }

    public static DateTime getWeekOfStart(DateTime dateTime, int firstDayNum) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        Assert.state(firstDayNum >= ONE && firstDayNum <= SEVEN
                , "Parameter \"firstDayNum\" must >= 1 and <= 7. ");
        DateTime result = DateUtils.getDayOfStart(dateTime);
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
        DateTime result = DateUtils.getWeekOfStart(dateTime, firstDayNum);
        result.addDay(SEVEN).addMillisecond(MINUS_ONE);
        return result;
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

        return DateUtils.format(new Date());
    }

    public static String format(String pattern) {

        return DateUtils.format(new Date(), pattern);
    }

    public static String format(Date date) {

        return getDateProvider().format(date);
    }

    public static String format(Long timestamp) {

        return DateUtils.format(DateUtils.parse(timestamp));
    }

    public static String format(DateTime dateTime) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        return DateUtils.format(dateTime.getDate());
    }

    public static String format(Date date, String pattern) {

        return getDateProvider().format(date, pattern);
    }

    public static String format(Long timestamp, String pattern) {
        Date date = DateUtils.parse(timestamp);
        return DateUtils.format(date, pattern);
    }

    public static String format(DateTime dateTime, String pattern) {
        Assert.notNull(dateTime, "Parameter \"dateTime\" must not null. ");
        return DateUtils.format(dateTime.getDate(), pattern);
    }

}
