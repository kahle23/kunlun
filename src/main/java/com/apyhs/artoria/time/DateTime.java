package com.apyhs.artoria.time;

import com.apyhs.artoria.util.Assert;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Date time object.
 * @author Kahle
 */
public class DateTime {

    public static DateTime create() {
        return new DateTime();
    }

    public static DateTime create(Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        DateTime dateTime = new DateTime();
        return dateTime.setDate(date);
    }

    public static DateTime create(Calendar calendar) {
        Assert.notNull(calendar, "Parameter \"calendar\" must not null. ");
        return new DateTime(calendar);
    }

    public static DateTime create(Long timestamp) {
        DateTime dateTime = new DateTime();
        timestamp = timestamp != null ? timestamp : 0;
        return dateTime.setTimeInMillis(timestamp);
    }

    public static DateTime create(String dateString) throws ParseException {
        Date date = DateUtils.parse(dateString);
        return new DateTime().setDate(date);
    }

    public static DateTime create(String dateString, String pattern) throws ParseException {
        Date date = DateUtils.parse(dateString, pattern);
        return new DateTime().setDate(date);
    }

    public static DateTime create(int year, int month, int day) {
        return new DateTime().setYear(year).setMonth(month)
                .setDay(day).setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
    }

    public static DateTime create(int year, int month, int day, int hour, int minute, int second) {
        return new DateTime().setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(0);
    }

    public static DateTime create(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        return new DateTime().setYear(year).setMonth(month).setDay(day)
                .setHour(hour).setMinute(minute).setSecond(second).setMillisecond(millisecond);
    }

    private static final Calendar CALENDAR_TEMPLATE = Calendar.getInstance();
    private Calendar calendar;

    private DateTime() {
        calendar = (Calendar) CALENDAR_TEMPLATE.clone();
        calendar.setTimeInMillis(System.currentTimeMillis());
    }

    private DateTime(Calendar calendar) {
        Assert.notNull(calendar, "Parameter \"calendar\" must not null. ");
        this.calendar = calendar;
    }

    public Date getDate() {
        return calendar.getTime();
    }

    public DateTime setDate(Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        calendar.setTime(date);
        return this;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public DateTime setCalendar(Calendar calendar) {
        Assert.notNull(calendar, "Parameter \"calendar\" must not null. ");
        this.calendar = calendar;
        return this;
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

    public DateTime setTimeInMillis(long timestamp) {
        calendar.setTimeInMillis(timestamp);
        return this;
    }

    public long getTimeInSeconds() {
        long millis = calendar.getTimeInMillis();
        return millis / 1000L;
    }

    public DateTime setTimeInSeconds(long unixTimestamp) {
        long millis = unixTimestamp * 1000L;
        calendar.setTimeInMillis(millis);
        return this;
    }

    public DateTime addYear(int addYear) {
        calendar.set(Calendar.YEAR, getYear() + addYear);
        return this;
    }

    public DateTime addMonth(int addMonth) {
        calendar.set(Calendar.MONTH, getMonth() - 1 + addMonth);
        return this;
    }

    public DateTime addDay(int addDay) {
        calendar.set(Calendar.DATE, getDay() + addDay);
        return this;
    }

    public DateTime addHour(int addHour) {
        calendar.set(Calendar.HOUR_OF_DAY, getHour() + addHour);
        return this;
    }

    public DateTime addMinute(int addMinute) {
        calendar.set(Calendar.MINUTE, getMinute() + addMinute);
        return this;
    }

    public DateTime addSecond(int addSecond) {
        calendar.set(Calendar.SECOND, getSecond() + addSecond);
        return this;
    }

    public DateTime addMillisecond(int addMillisecond) {
        calendar.set(Calendar.MILLISECOND, getMillisecond() + addMillisecond);
        return this;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public DateTime setYear(int year) {
        calendar.set(Calendar.YEAR, year);
        return this;
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public DateTime setMonth(int month) {
        boolean b = this.getYear() == 0 || this.getYear() == 1;
        month = b && month == 0 ? month : month - 1;
        calendar.set(Calendar.MONTH, month);
        return this;
    }

    public int getDay() {
        return calendar.get(Calendar.DATE);
    }

    public DateTime setDay(int day) {
        boolean by = this.getYear() == 0 || this.getYear() == 1;
        boolean bm = this.getMonth() == 0 || this.getMonth() == 1;
        day = by && bm && day == 0 ? 1 : day;
        calendar.set(Calendar.DATE, day);
        return this;
    }

    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public DateTime setHour(int hour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public DateTime setMinute(int minute) {
        calendar.set(Calendar.MINUTE, minute);
        return this;
    }

    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    public DateTime setSecond(int second) {
        calendar.set(Calendar.SECOND, second);
        return this;
    }

    public int getMillisecond() {
        return calendar.get(Calendar.MILLISECOND);
    }

    public DateTime setMillisecond(int millisecond) {
        calendar.set(Calendar.MILLISECOND, millisecond);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (this == o) { return true; }
        if (o instanceof DateTime) {
            Calendar cal = this.getCalendar();
            Calendar cal1 = ((DateTime) o).getCalendar();
            return DateUtils.equals(cal, cal1);
        }
        return false;
    }

    @Override
    public String toString() {
        Date date = this.getDate();
        return DateUtils.format(date);
    }

}
