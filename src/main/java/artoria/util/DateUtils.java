package artoria.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date tools.
 * @author Kahle
 */
public class DateUtils {

    private static final String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

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
        return DateUtils.parse(dateString, DEFAULT_TIME_PATTERN);
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
        return DateUtils.format(timestamp, DEFAULT_TIME_PATTERN);
    }

    public static String format(String pattern) {
        return DateUtils.format(new Date(), pattern);
    }

    public static String format(Date date) {
        return DateUtils.format(date, DEFAULT_TIME_PATTERN);
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

    public static DateUtils create() {
        return new DateUtils();
    }

    public static DateUtils create(Date date) {
        Assert.notNull(date, "Date must is not null. ");
        DateUtils dateUtils = new DateUtils();
        return dateUtils.setDate(date);
    }

    public static DateUtils create(Calendar calendar) {
        Assert.notNull(calendar, "Calendar must is not null. ");
        return new DateUtils(calendar);
    }

    public static DateUtils create(Long timestamp) {
        DateUtils dateUtils = new DateUtils();
        timestamp = timestamp != null ? timestamp : 0;
        return dateUtils.setTimeInMillis(timestamp);
    }

    public static DateUtils create(String dateString) throws ParseException {
        Date date = DateUtils.parse(dateString);
        return new DateUtils().setDate(date);
    }

    public static DateUtils create(String dateString, String pattern) throws ParseException {
        Date date = DateUtils.parse(dateString, pattern);
        return new DateUtils().setDate(date);
    }

    public static DateUtils create(int year, int month, int day) {
        return new DateUtils().setYear(year).setMonth(month)
                .setDay(day).setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
    }

    public static DateUtils create(int year, int month, int day, int hour, int minute, int second) {
        return new DateUtils().setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(0);
    }

    public static DateUtils create(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        return new DateUtils().setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(millisecond);
    }

    private static final Calendar CALENDAR_TEMPLATE = Calendar.getInstance();
    private Calendar calendar;

    private DateUtils() {
        calendar = (Calendar) CALENDAR_TEMPLATE.clone();
        calendar.setTimeInMillis(System.currentTimeMillis());
    }

    private DateUtils(Calendar calendar) {
        Assert.notNull(calendar, "Calendar must is not null. ");
        this.calendar = calendar;
    }

    public Date getDate() {
        return calendar.getTime();
    }

    public DateUtils setDate(Date date) {
        Assert.notNull(date, "Date must is not null. ");
        calendar.setTime(date);
        return this;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public DateUtils setCalendar(Calendar calendar) {
        Assert.notNull(calendar, "Calendar must is not null. ");
        this.calendar = calendar;
        return this;
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

    public DateUtils setTimeInMillis(long timestamp) {
        calendar.setTimeInMillis(timestamp);
        return this;
    }

    public long getTimeInSeconds() {
        long millis = calendar.getTimeInMillis();
        return millis / 1000L;
    }

    public DateUtils setTimeInSeconds(long unixTimestamp) {
        long millis = unixTimestamp * 1000L;
        calendar.setTimeInMillis(millis);
        return this;
    }

    public DateUtils addYear(int addYear) {
        calendar.set(Calendar.YEAR, getYear() + addYear);
        return this;
    }

    public DateUtils addMonth(int addMonth) {
        calendar.set(Calendar.MONTH, getMonth() - 1 + addMonth);
        return this;
    }

    public DateUtils addDay(int addDay) {
        calendar.set(Calendar.DATE, getDay() + addDay);
        return this;
    }

    public DateUtils addHour(int addHour) {
        calendar.set(Calendar.HOUR_OF_DAY, getHour() + addHour);
        return this;
    }

    public DateUtils addMinute(int addMinute) {
        calendar.set(Calendar.MINUTE, getMinute() + addMinute);
        return this;
    }

    public DateUtils addSecond(int addSecond) {
        calendar.set(Calendar.SECOND, getSecond() + addSecond);
        return this;
    }

    public DateUtils addMillisecond(int addMillisecond) {
        calendar.set(Calendar.MILLISECOND, getMillisecond() + addMillisecond);
        return this;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public DateUtils setYear(int year) {
        calendar.set(Calendar.YEAR, year);
        return this;
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public DateUtils setMonth(int month) {
        boolean b = this.getYear() == 0 || this.getYear() == 1;
        month = b && month == 0 ? month : month - 1;
        calendar.set(Calendar.MONTH, month);
        return this;
    }

    public int getDay() {
        return calendar.get(Calendar.DATE);
    }

    public DateUtils setDay(int day) {
        boolean by = this.getYear() == 0 || this.getYear() == 1;
        boolean bm = this.getMonth() == 0 || this.getMonth() == 1;
        day = by && bm && day == 0 ? 1 : day;
        calendar.set(Calendar.DATE, day);
        return this;
    }

    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public DateUtils setHour(int hour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public DateUtils setMinute(int minute) {
        calendar.set(Calendar.MINUTE, minute);
        return this;
    }

    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    public DateUtils setSecond(int second) {
        calendar.set(Calendar.SECOND, second);
        return this;
    }

    public int getMillisecond() {
        return calendar.get(Calendar.MILLISECOND);
    }

    public DateUtils setMillisecond(int millisecond) {
        calendar.set(Calendar.MILLISECOND, millisecond);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (this == o) { return true; }
        if (o instanceof DateUtils) {
            Calendar cal = this.getCalendar();
            Calendar cal1 = ((DateUtils) o).getCalendar();
            return DateUtils.equals(cal, cal1);
        }
        return false;
    }

    public String toString(String pattern) {
        Date date = this.getDate();
        return DateUtils.format(date, pattern);
    }

    @Override
    public String toString() {
        Date date = this.getDate();
        return DateUtils.format(date);
    }

}
