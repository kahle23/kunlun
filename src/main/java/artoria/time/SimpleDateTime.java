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

        setCalendar(calendar);
    }

    public SimpleDateTime(Date date) {
        this();
        setDate(date);
    }

    @Override
    public Date getDate() {

        return calendar.getTime();
    }

    @Override
    public DateTime setDate(Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        calendar.setTime(date);
        return this;
    }

    @Override
    public Calendar getCalendar() {

        return calendar;
    }

    @Override
    public DateTime setCalendar(Calendar calendar) {
        Assert.notNull(calendar, "Parameter \"calendar\" must not null. ");
        this.calendar = calendar;
        return this;
    }

    @Override
    public long getTimeInMillis() {

        return calendar.getTimeInMillis();
    }

    @Override
    public DateTime setTimeInMillis(long timestamp) {
        calendar.setTimeInMillis(timestamp);
        return this;
    }

    @Override
    public long getTimeInSeconds() {
        long millis = calendar.getTimeInMillis();
        return millis / 1000L;
    }

    @Override
    public DateTime setTimeInSeconds(long unixTimestamp) {
        long millis = unixTimestamp * 1000L;
        calendar.setTimeInMillis(millis);
        return this;
    }

    @Override
    public DateTime addYear(int addYear) {
        calendar.add(Calendar.YEAR, addYear);
        return this;
    }

    @Override
    public DateTime addMonth(int addMonth) {
        calendar.add(Calendar.MONTH, addMonth);
        return this;
    }

    @Override
    public DateTime addDay(int addDay) {
        calendar.add(Calendar.DATE, addDay);
        return this;
    }

    @Override
    public DateTime addHour(int addHour) {
        calendar.add(Calendar.HOUR_OF_DAY, addHour);
        return this;
    }

    @Override
    public DateTime addMinute(int addMinute) {
        calendar.add(Calendar.MINUTE, addMinute);
        return this;
    }

    @Override
    public DateTime addSecond(int addSecond) {
        calendar.add(Calendar.SECOND, addSecond);
        return this;
    }

    @Override
    public DateTime addMillisecond(int addMillisecond) {
        calendar.add(Calendar.MILLISECOND, addMillisecond);
        return this;
    }

    @Override
    public DateTime addDayOfWeek(int addDayOfWeek) {
        calendar.add(Calendar.DAY_OF_WEEK, addDayOfWeek);
        return this;
    }

    @Override
    public DateTime addDayOfWeekInMonth(int addDayOfWeekInMonth) {
        calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, addDayOfWeekInMonth);
        return this;
    }

    @Override
    public DateTime addDayOfYear(int addDayOfYear) {
        calendar.add(Calendar.DAY_OF_YEAR, addDayOfYear);
        return this;
    }

    @Override
    public DateTime addWeekOfMonth(int addWeekOfMonth) {
        calendar.add(Calendar.WEEK_OF_MONTH, addWeekOfMonth);
        return this;
    }

    @Override
    public DateTime addWeekOfYear(int addWeekOfYear) {
        calendar.add(Calendar.WEEK_OF_YEAR, addWeekOfYear);
        return this;
    }

    @Override
    public int getYear() {

        return calendar.get(Calendar.YEAR);
    }

    @Override
    public DateTime setYear(int year) {
        calendar.set(Calendar.YEAR, year);
        return this;
    }

    @Override
    public int getMonth() {

        return calendar.get(Calendar.MONTH) + 1;
    }

    @Override
    public DateTime setMonth(int month) {
        boolean b = getYear() == 0 || getYear() == 1;
        month = b && month == 0 ? month : month - 1;
        calendar.set(Calendar.MONTH, month);
        return this;
    }

    @Override
    public int getDay() {

        return calendar.get(Calendar.DATE);
    }

    @Override
    public DateTime setDay(int day) {
        boolean by = getYear() == 0 || getYear() == 1;
        boolean bm = getMonth() == 0 || getMonth() == 1;
        day = by && bm && day == 0 ? 1 : day;
        // This is a synonym for DAY_OF_MONTH.
        calendar.set(Calendar.DATE, day);
        return this;
    }

    @Override
    public int getHour() {

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public DateTime setHour(int hour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    @Override
    public int getMinute() {

        return calendar.get(Calendar.MINUTE);
    }

    @Override
    public DateTime setMinute(int minute) {
        calendar.set(Calendar.MINUTE, minute);
        return this;
    }

    @Override
    public int getSecond() {

        return calendar.get(Calendar.SECOND);
    }

    @Override
    public DateTime setSecond(int second) {
        calendar.set(Calendar.SECOND, second);
        return this;
    }

    @Override
    public int getMillisecond() {

        return calendar.get(Calendar.MILLISECOND);
    }

    @Override
    public DateTime setMillisecond(int millisecond) {
        calendar.set(Calendar.MILLISECOND, millisecond);
        return this;
    }

    @Override
    public int getDayOfWeek() {

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public DateTime setDayOfWeek(int dayOfWeek) {
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return this;
    }

    @Override
    public int getDayOfWeekInMonth() {

        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    @Override
    public DateTime setDayOfWeekInMonth(int dayOfWeekInMonth) {
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
        return this;
    }

    @Override
    public int getDayOfYear() {

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public DateTime setDayOfYear(int dayOfYear) {
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return this;
    }

    @Override
    public int getWeekOfMonth() {

        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    @Override
    public DateTime setWeekOfMonth(int weekOfMonth) {
        calendar.set(Calendar.WEEK_OF_MONTH, weekOfMonth);
        return this;
    }

    @Override
    public int getWeekOfYear() {

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    @Override
    public DateTime setWeekOfYear(int weekOfYear) {
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        return this;
    }

    @Override
    public boolean before(DateTime when) {

        return getTimeInMillis() < when.getTimeInMillis();
    }

    @Override
    public boolean after(DateTime when) {

        return getTimeInMillis() > when.getTimeInMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (this == o) { return true; }
        if (o instanceof SimpleDateTime) {
            Calendar cal = getCalendar();
            Calendar cal1 = ((SimpleDateTime) o).getCalendar();
            return DateUtils.equals(cal, cal1);
        }
        return false;
    }

    @Override
    public String toString() {
        Date date = getDate();
        return DateUtils.format(date);
    }

}
