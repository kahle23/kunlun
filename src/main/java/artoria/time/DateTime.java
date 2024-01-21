package artoria.time;

import artoria.core.Time;

import java.util.Calendar;
import java.util.Date;

/**
 * The date time interface.
 * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
 * @author Kahle
 */
public interface DateTime extends Time, java.io.Serializable {

    /**
     * Get date from DateTime object.
     * @return The date object from DateTime
     */
    Date getDate();

    /**
     * Set date to DateTime object.
     * @param date The date object you want
     * @return The current date time object
     */
    DateTime setDate(Date date);

    /**
     * Get calendar from DateTime object.
     * @return The calendar object from DateTime
     */
    Calendar getCalendar();

    /**
     * Set calendar to DateTime object.
     * @param calendar The calendar object you want
     * @return The current date time object
     */
    DateTime setCalendar(Calendar calendar);

    /**
     * The current time as UTC milliseconds from the epoch.
     * @return The time in milliseconds
     * @see <a href="https://en.wikipedia.org/wiki/Epoch_(computing)">Epoch (computing)</a>
     */
    long getTimeInMillis();

    /**
     * Set the time in milliseconds.
     * @param timeInMillis The time in milliseconds
     * @return The current date time object
     */
    DateTime setTimeInMillis(long timeInMillis);

    /**
     * The current time as UTC seconds from the epoch.
     * @return The time in seconds
     * @see <a href="https://en.wikipedia.org/wiki/Epoch_(computing)">Epoch (computing)</a>
     */
    long getTimeInSeconds();

    /**
     * Set the time in seconds.
     * @param timeInSeconds The time in seconds
     * @return The current date time object
     */
    DateTime setTimeInSeconds(long timeInSeconds);

    /**
     * Add year for DateTime object.
     * @param addYear Year you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addYear(int addYear);

    /**
     * Add month for DateTime object.
     * @param addMonth Month you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addMonth(int addMonth);

    /**
     * Add day for DateTime object.
     * @param addDay Day you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addDay(int addDay);

    /**
     * Add hour for DateTime object.
     * @param addHour Hour you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addHour(int addHour);

    /**
     * Add minute for DateTime object.
     * @param addMinute Minute you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addMinute(int addMinute);

    /**
     * Add second for DateTime object.
     * @param addSecond Second you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addSecond(int addSecond);

    /**
     * Add millisecond for DateTime object.
     * @param addMillisecond Millisecond you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addMillisecond(int addMillisecond);

    /**
     * Add day of week for DateTime object.
     * @param addDayOfWeek Day of week you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addDayOfWeek(int addDayOfWeek);

    /**
     * Add day of week in month for DateTime object.
     * @param addDayOfWeekInMonth Day of week in month you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addDayOfWeekInMonth(int addDayOfWeekInMonth);

    /**
     * Add day of year for DateTime object.
     * @param addDayOfYear Day of year you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addDayOfYear(int addDayOfYear);

    /**
     * Add week of month for DateTime object.
     * @param addWeekOfMonth Week of month you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addWeekOfMonth(int addWeekOfMonth);

    /**
     * Add week of year for DateTime object.
     * @param addWeekOfYear Week of year you want to add (the num can minus)
     * @return The current date time object
     */
    DateTime addWeekOfYear(int addWeekOfYear);

    /**
     * Get year from DateTime object.
     * @return The year from DateTime
     */
    int getYear();

    /**
     * Set year to DateTime object.
     * @param year The year you want
     * @return The current date time object
     */
    DateTime setYear(int year);

    /**
     * Get month from DateTime object.
     * @return The month from DateTime
     */
    int getMonth();

    /**
     * Set month to DateTime object.
     * @param month The month you want
     * @return The current date time object
     */
    DateTime setMonth(int month);

    /**
     * Get day from DateTime object.
     * @return The day from DateTime
     */
    int getDay();

    /**
     * Set day to DateTime object.
     * @param day The day you want
     * @return The current date time object
     */
    DateTime setDay(int day);

    /**
     * Get hour from DateTime object.
     * @return The hour from DateTime
     */
    int getHour();

    /**
     * Set hour to DateTime object.
     * @param hour The hour you want
     * @return The current date time object
     */
    DateTime setHour(int hour);

    /**
     * Get minute from DateTime object.
     * @return The minute from DateTime
     */
    int getMinute();

    /**
     * Set minute to DateTime object.
     * @param minute The minute you want
     * @return The current date time object
     */
    DateTime setMinute(int minute);

    /**
     * Get second from DateTime object.
     * @return The second from DateTime
     */
    int getSecond();

    /**
     * Set second to DateTime object.
     * @param second The second you want
     * @return The current date time object
     */
    DateTime setSecond(int second);

    /**
     * Get millisecond from DateTime object.
     * @return The millisecond from DateTime
     */
    int getMillisecond();

    /**
     * Set millisecond to DateTime object.
     * @param millisecond The millisecond you want
     * @return The current date time object
     */
    DateTime setMillisecond(int millisecond);

    /**
     * Get day of week from DateTime object.
     * @return The day of week from DateTime
     */
    int getDayOfWeek();

    /**
     * Set day of week to DateTime object.
     * @param dayOfWeek The day of week you want
     * @return The current date time object
     */
    DateTime setDayOfWeek(int dayOfWeek);

    /**
     * Get day of week in month from DateTime object.
     * @return The day of week in month from DateTime
     */
    int getDayOfWeekInMonth();

    /**
     * Set day of week in month to DateTime object.
     * @param dayOfWeekInMonth The day of week in month you want
     * @return The current date time object
     */
    DateTime setDayOfWeekInMonth(int dayOfWeekInMonth);

    /**
     * Get day of year from DateTime object.
     * @return The day of year from DateTime
     */
    int getDayOfYear();

    /**
     * Set day of year to DateTime object.
     * @param dayOfYear The day of year you want
     * @return The current date time object
     */
    DateTime setDayOfYear(int dayOfYear);

    /**
     * Get week of month from DateTime object.
     * @return The week of month from DateTime
     */
    int getWeekOfMonth();

    /**
     * Set week of month to DateTime object.
     * @param weekOfMonth The week of month you want
     * @return The current date time object
     */
    DateTime setWeekOfMonth(int weekOfMonth);

    /**
     * Get week of year from DateTime object.
     * @return The week of year from DateTime
     */
    int getWeekOfYear();

    /**
     * Set week of year to DateTime object.
     * @param weekOfYear The week of year you want
     * @return The current date time object
     */
    DateTime setWeekOfYear(int weekOfYear);

    /**
     * Judge this DateTime is before the input.
     * @param when The DateTime you want judge
     * @return If true this is before input, false is not
     */
    boolean before(DateTime when);

    /**
     * Judge this DateTime is after the input.
     * @param when The DateTime you want judge
     * @return If true this is after input, false is not
     */
    boolean after(DateTime when);

    /**
     * Judge this DateTime is equals the input.
     * @param o The DateTime you want judge
     * @return If true this is equals input, false is not
     */
    @Override
    boolean equals(Object o);

    /**
     * Formatting output DateTime to string.
     * @return The DateTime formatted string
     */
    @Override
    String toString();

}
