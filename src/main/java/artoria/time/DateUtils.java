package artoria.time;

import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static artoria.common.Constants.*;

/**
 * Date tools.
 * @author Kahle
 */
public class DateUtils {
    private static final Class<? extends DateTime> DEFAULT_TIME_TYPE;
    private static final DateFormatter DEFAULT_DATE_FORMATTER;
    private static final DateParser DEFAULT_DATE_PARSER;
    private static Logger log = LoggerFactory.getLogger(DateUtils.class);
    private static Class<? extends DateTime> timeType;
    private static DateFormatter dateFormatter;
    private static DateParser dateParser;

    static {
        SimpleDateFormatter formatter = new SimpleDateFormatter();
        DEFAULT_DATE_FORMATTER = formatter;
        DEFAULT_DATE_PARSER = formatter;
        DEFAULT_TIME_TYPE = SimpleDateTime.class;
    }

    public static Class<? extends DateTime> getTimeType() {

        return timeType != null ? timeType : DEFAULT_TIME_TYPE;
    }

    public static void setTimeType(Class<? extends DateTime> timeType) {
        Assert.notNull(timeType, "Parameter \"timeType\" must not null. ");
        log.info("Set time type: {}", timeType.getName());
        DateUtils.timeType = timeType;
    }

    public static DateFormatter getDateFormatter() {

        return dateFormatter != null ? dateFormatter : DEFAULT_DATE_FORMATTER;
    }

    public static void setDateFormatter(DateFormatter formatter) {
        Assert.notNull(formatter, "Parameter \"formatter\" must not null. ");
        log.info("Set date formatter: {}", formatter.getClass().getName());
        DateUtils.dateFormatter = formatter;
    }

    public static DateParser getDateParser() {

        return dateParser != null ? dateParser : DEFAULT_DATE_PARSER;
    }

    public static void setDateParser(DateParser parser) {
        Assert.notNull(parser, "Parameter \"parser\" must not null. ");
        log.info("Set date parser: {}", parser.getClass().getName());
        DateUtils.dateParser = parser;
    }

    public static DateTime create() {
        try {
            return getTimeType().newInstance();
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
        timestamp = timestamp != null ? timestamp : ZERO;
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

    public static long getTimestamp() {

        return System.currentTimeMillis();
    }

    public static long getUnixTimestamp() {
        long millis = System.currentTimeMillis();
        return millis / ONE_THOUSAND;
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

    public static Date parse(String dateString) throws ParseException {

        return DateUtils.parse(dateString, DEFAULT_DATE_PATTERN);
    }

    public static Date parse(String dateString, String pattern) throws ParseException {

        return getDateParser().parse(dateString, pattern);
    }

    public static String format() {

        return DateUtils.format(new Date());
    }

    public static String format(String pattern) {

        return DateUtils.format(new Date(), pattern);
    }

    public static String format(Date date) {

        return DateUtils.format(date, DEFAULT_DATE_PATTERN);
    }

    public static String format(Long timestamp) {

        return DateUtils.format(timestamp, DEFAULT_DATE_PATTERN);
    }

    public static String format(DateTime dateTime) {

        return DateUtils.format(dateTime, DEFAULT_DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {

        return getDateFormatter().format(date, pattern);
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
