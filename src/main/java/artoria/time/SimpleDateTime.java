package artoria.time;

import artoria.util.Assert;

import java.util.Calendar;
import java.util.Date;

/**
 * Date time simple implement by jdk.
 * @author Kahle
 */
public class SimpleDateTime implements DateTime {
    private static final Calendar CALENDAR_TEMPLATE = Calendar.getInstance();
    private Calendar calendar;

    public SimpleDateTime() {
        this.calendar = (Calendar) CALENDAR_TEMPLATE.clone();
        this.calendar.setTimeInMillis(System.currentTimeMillis());
    }

    public SimpleDateTime(Calendar calendar) {

        this.setCalendar(calendar);
    }

    public SimpleDateTime(Date date) {
        this();
        this.setDate(date);
    }

    @Override
    public Date getDate() {

        return this.calendar.getTime();
    }

    @Override
    public DateTime setDate(Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        this.calendar.setTime(date);
        return this;
    }

    @Override
    public Calendar getCalendar() {

        return this.calendar;
    }

    @Override
    public DateTime setCalendar(Calendar calendar) {
        Assert.notNull(calendar, "Parameter \"calendar\" must not null. ");
        this.calendar = calendar;
        return this;
    }

    @Override
    public long getTimeInMillis() {

        return this.calendar.getTimeInMillis();
    }

    @Override
    public DateTime setTimeInMillis(long timestamp) {
        this.calendar.setTimeInMillis(timestamp);
        return this;
    }

    @Override
    public long getTimeInSeconds() {
        long millis = this.calendar.getTimeInMillis();
        return millis / 1000L;
    }

    @Override
    public DateTime setTimeInSeconds(long unixTimestamp) {
        long millis = unixTimestamp * 1000L;
        this.calendar.setTimeInMillis(millis);
        return this;
    }

    @Override
    public DateTime addYear(int addYear) {
        this.calendar.set(Calendar.YEAR, getYear() + addYear);
        return this;
    }

    @Override
    public DateTime addMonth(int addMonth) {
        this.calendar.set(Calendar.MONTH, getMonth() - 1 + addMonth);
        return this;
    }

    @Override
    public DateTime addDay(int addDay) {
        this.calendar.set(Calendar.DATE, getDay() + addDay);
        return this;
    }

    @Override
    public DateTime addHour(int addHour) {
        this.calendar.set(Calendar.HOUR_OF_DAY, getHour() + addHour);
        return this;
    }

    @Override
    public DateTime addMinute(int addMinute) {
        this.calendar.set(Calendar.MINUTE, getMinute() + addMinute);
        return this;
    }

    @Override
    public DateTime addSecond(int addSecond) {
        this.calendar.set(Calendar.SECOND, getSecond() + addSecond);
        return this;
    }

    @Override
    public DateTime addMillisecond(int addMillisecond) {
        this.calendar.set(Calendar.MILLISECOND, getMillisecond() + addMillisecond);
        return this;
    }

    @Override
    public int getYear() {

        return this.calendar.get(Calendar.YEAR);
    }

    @Override
    public DateTime setYear(int year) {
        this.calendar.set(Calendar.YEAR, year);
        return this;
    }

    @Override
    public int getMonth() {

        return this.calendar.get(Calendar.MONTH) + 1;
    }

    @Override
    public DateTime setMonth(int month) {
        boolean b = this.getYear() == 0 || this.getYear() == 1;
        month = b && month == 0 ? month : month - 1;
        this.calendar.set(Calendar.MONTH, month);
        return this;
    }

    @Override
    public int getDay() {

        return this.calendar.get(Calendar.DATE);
    }

    @Override
    public DateTime setDay(int day) {
        boolean by = this.getYear() == 0 || this.getYear() == 1;
        boolean bm = this.getMonth() == 0 || this.getMonth() == 1;
        day = by && bm && day == 0 ? 1 : day;
        this.calendar.set(Calendar.DATE, day);
        return this;
    }

    @Override
    public int getHour() {

        return this.calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public DateTime setHour(int hour) {
        this.calendar.set(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    @Override
    public int getMinute() {

        return this.calendar.get(Calendar.MINUTE);
    }

    @Override
    public DateTime setMinute(int minute) {
        this.calendar.set(Calendar.MINUTE, minute);
        return this;
    }

    @Override
    public int getSecond() {

        return this.calendar.get(Calendar.SECOND);
    }

    @Override
    public DateTime setSecond(int second) {
        this.calendar.set(Calendar.SECOND, second);
        return this;
    }

    @Override
    public int getMillisecond() {

        return this.calendar.get(Calendar.MILLISECOND);
    }

    @Override
    public DateTime setMillisecond(int millisecond) {
        this.calendar.set(Calendar.MILLISECOND, millisecond);
        return this;
    }

    @Override
    public boolean before(DateTime when) {

        return this.getTimeInMillis() < when.getTimeInMillis();
    }

    @Override
    public boolean after(DateTime when) {

        return this.getTimeInMillis() > when.getTimeInMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (this == o) { return true; }
        if (o instanceof SimpleDateTime) {
            Calendar cal = this.getCalendar();
            Calendar cal1 = ((SimpleDateTime) o).getCalendar();
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
