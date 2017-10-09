package saber;

import java.util.Calendar;
import java.util.Date;

public class Time {
    private Calendar calendar;

    public Time() {
        calendar = Calendar.getInstance();
    }

    public Time(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    public Time(Calendar calendar) {
        this.calendar = calendar;
    }

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

    @Override
    public String toString() {
        return TimeUtils.format(calendar.getTime());
    }

}
