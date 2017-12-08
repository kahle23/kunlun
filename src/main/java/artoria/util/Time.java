package artoria.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Kahle
 */
public class Time {
    private static final String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

    public static Time create() {
        return new Time().setCalendar(Calendar.getInstance());
    }

    public static Time create(Object time) throws ParseException {
        return Time.create(time, DEFAULT_TIME_PATTERN);
    }

    public static Time create(Object time, String pattern) throws ParseException {
        if (time instanceof String) {
            return Time.create((String) time, pattern);
        }
        else if (time instanceof Long) {
            return Time.create((Long) time);
        }
        else if (long.class.isInstance(time)) {
            return Time.create((long) time);
        }
        else if (time instanceof Time) {
            return (Time) time;
        }
        else if (time instanceof Date) {
            return Time.create((Date) time);
        }
        else if (time instanceof Calendar) {
            return Time.create((Calendar) time);
        }
        else {
            return null;
        }
    }

    public static Time create(Long ts) {
        return Time.create().setTimestamp(ts);
    }

    public static Time create(Date date) {
        return Time.create().setDate(date);
    }

    public static Time create(Calendar calendar) {
        return new Time().setCalendar(calendar);
    }

    public static Time create(String timeString) throws ParseException {
        return Time.create(timeString, DEFAULT_TIME_PATTERN);
    }

    public static Time create(String timeString, String pattern) throws ParseException {
        Time time = Time.create();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = dateFormat.parse(timeString);
        return time.setDate(date);
    }

    public static Time create(int year, int month, int day) {
        return Time.create().setYear(year).setMonth(month)
                .setDay(day).setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
    }

    public static Time create(int year, int month, int day, int hour, int minute, int second) {
        return Time.create().setYear(year).setMonth(month)
                .setDay(day).setHour(hour).setMinute(minute).setSecond(second).setMillisecond(0);
    }

    public static Time create(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        return Time.create().setYear(year).setMonth(month)
                .setDay(day).setHour(hour).setMinute(minute).setSecond(second).setMillisecond(millisecond);
    }

    public static boolean equal(Date d1, Date d2) {
        return d1 == null ? d2 == null : d2 != null && (d1 == d2 || d1.equals(d2));
    }

    private Calendar calendar;

    private Time() {}

    public Date getDate() {
        return calendar.getTime();
    }

    public Time setDate(Date date) {
        calendar.setTime(date);
        return this;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Time setCalendar(Calendar calendar) {
        this.calendar = calendar;
        return this;
    }

    public long getTimestamp() {
        return calendar.getTimeInMillis();
    }

    public Time setTimestamp(long timestamp) {
        calendar.setTimeInMillis(timestamp);
        return this;
    }

    public long getUnixTimestamp() {
        return calendar.getTimeInMillis() / 1000;
    }

    public Time setUnixTimestamp(long unixTimestamp) {
        calendar.setTimeInMillis(unixTimestamp * 1000L);
        return this;
    }

    public Time addYear(int addYear) {
        calendar.set(Calendar.YEAR, getYear() + addYear);
        return this;
    }

    public Time addMonth(int addMonth) {
        calendar.set(Calendar.MONTH, getMonth() - 1 + addMonth);
        return this;
    }

    public Time addDay(int addDay) {
        calendar.set(Calendar.DATE, getDay() + addDay);
        return this;
    }

    public Time addHour(int addHour) {
        calendar.set(Calendar.HOUR_OF_DAY, getHour() + addHour);
        return this;
    }

    public Time addMinute(int addMinute) {
        calendar.set(Calendar.MINUTE, getMinute() + addMinute);
        return this;
    }

    public Time addSecond(int addSecond) {
        calendar.set(Calendar.SECOND, getSecond() + addSecond);
        return this;
    }

    public Time addMillisecond(int addMillisecond) {
        calendar.set(Calendar.MILLISECOND, getMillisecond() + addMillisecond);
        return this;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public Time setYear(int year) {
        calendar.set(Calendar.YEAR, year);
        return this;
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public Time setMonth(int month) {
        month = (getYear() == 0 || getYear() == 1) && month == 0 ? month : month - 1;
        calendar.set(Calendar.MONTH, month);
        return this;
    }

    public int getDay() {
        return calendar.get(Calendar.DATE);
    }

    public Time setDay(int day) {
        day = (getYear() == 0 || getYear() == 1) && (getMonth() == 0 || getMonth() == 1) && day == 0 ? 1 : day;
        calendar.set(Calendar.DATE, day);
        return this;
    }

    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public Time setHour(int hour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public Time setMinute(int minute) {
        calendar.set(Calendar.MINUTE, minute);
        return this;
    }

    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    public Time setSecond(int second) {
        calendar.set(Calendar.SECOND, second);
        return this;
    }

    public int getMillisecond() {
        return calendar.get(Calendar.MILLISECOND);
    }

    public Time setMillisecond(int millisecond) {
        calendar.set(Calendar.MILLISECOND, millisecond);
        return this;
    }

    public boolean equal(Time time) {
        return Time.equal(this.getDate(), time.getDate());
    }

    public String format() {
        return format(DEFAULT_TIME_PATTERN);
    }

    public String format(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(getDate());
    }

    @Override
    public String toString() {
        return format(DEFAULT_TIME_PATTERN);
    }

}
