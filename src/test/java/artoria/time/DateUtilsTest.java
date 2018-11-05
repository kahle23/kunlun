package artoria.time;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void testEquals() {
        System.out.println();

        Date date1 = DateUtils.create(1000, 1, 1).getDate();
        Date date2 = DateUtils.create(1000, 1, 1).getDate();
        System.out.println("date1 equals date2 is true ? result : " + DateUtils.equals(date1, date2));

        Calendar calendar1 = DateUtils.create(2000, 1, 1).getCalendar();
        Calendar calendar2 = DateUtils.create(2000, 1, 1).getCalendar();
        System.out.println("calendar1 equals calendar2 is true ? result : " + DateUtils.equals(calendar1, calendar2));

        System.out.println();
    }

    @Test
    public void testFormat() {
        System.out.println();

        String pattern = "yyyy-MM-dd'T'HH:mm:ss.Z";
        System.out.println("Test format, The date will new, The pattern is \"" + pattern + "\". ");
        System.out.println("The method format(), result : " + DateUtils.format());
        System.out.println("The method format(Long), result : " + DateUtils.format(new Date().getTime()));
        System.out.println("The method format(String), result : " + DateUtils.format(pattern));
        System.out.println("The method format(Date), result : " + DateUtils.format(new Date()));
        System.out.println("The method format(Long, String), result : " + DateUtils.format(new Date().getTime(), pattern));
        System.out.println("The method format(Date, String), result : " + DateUtils.format(new Date(), pattern));

        System.out.println();
    }

    @Test
    public void testParseFormat() throws ParseException {
        System.out.println();

        System.out.println("The method parse(Long), result : " + DateUtils.parse(DateUtils.create().getTimeInMillis()));
        System.out.println("The method parse(String), result : " + DateUtils.parse(DateUtils.create().toString()));
        System.out.println("The method parse(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result : "
                + DateUtils.format(DateUtils.parse(DateUtils.create().toString(), "yyyy-MM-dd HH:mm:ss SSS")));

        System.out.println();
    }

    @Test
    public void testTimestamp() {
        System.out.println();

        System.out.println("The method getTimestamp(), result : " + DateUtils.getTimestamp());
        System.out.println("The method getUnixTimestamp(), result : " + DateUtils.getUnixTimestamp());

        System.out.println();
    }

    @Test
    public void testAddAmount() {
        Date date = new Date();
        System.out.println(DateUtils.format(DateUtils.addYear(date, 10)));
        System.out.println(DateUtils.format(DateUtils.addMonth(date, 10)));
        System.out.println(DateUtils.format(DateUtils.addDay(date, 10)));
        System.out.println(DateUtils.format(DateUtils.addHour(date, 10)));
        System.out.println(DateUtils.format(DateUtils.addMinute(date, 10)));
        System.out.println(DateUtils.format(DateUtils.addSecond(date, 10)));
        System.out.println(DateUtils.format(DateUtils.addMillisecond(date, 10)));
    }

    @Test
    public void testGetAmount() {
        Date date = new Date();
        System.out.println(DateUtils.getYear(date));
        System.out.println(DateUtils.getMonth(date));
        System.out.println(DateUtils.getDay(date));
        System.out.println(DateUtils.getHour(date));
        System.out.println(DateUtils.getMinute(date));
        System.out.println(DateUtils.getSecond(date));
        System.out.println(DateUtils.getMillisecond(date));
    }

    @Test
    public void testSetAmount() {
        Date date = new Date();
        System.out.println(DateUtils.format(DateUtils.setYear(date, 10)));
        System.out.println(DateUtils.format(DateUtils.setMonth(date, 10)));
        System.out.println(DateUtils.format(DateUtils.setDay(date, 10)));
        System.out.println(DateUtils.format(DateUtils.setHour(date, 10)));
        System.out.println(DateUtils.format(DateUtils.setMinute(date, 10)));
        System.out.println(DateUtils.format(DateUtils.setSecond(date, 10)));
        System.out.println(DateUtils.format(DateUtils.setMillisecond(date, 10)));
    }

    @Test
    public void testOfStartOfEnd() {
        DateTime dateTime = DateUtils.create();
        System.out.println("Now time: " + DateUtils.format(dateTime));
        DateTime dayOfStart = DateUtils.getDayOfStart(dateTime);
        System.out.println("Day Of Start: " + DateUtils.format(dayOfStart));
        DateTime dayOfEnd = DateUtils.getDayOfEnd(dateTime);
        System.out.println("Day Of End: " + DateUtils.format(dayOfEnd));
        DateTime monthOfStart = DateUtils.getMonthOfStart(dateTime);
        System.out.println("Month Of Start: " + DateUtils.format(monthOfStart));
        DateTime monthOfEnd = DateUtils.getMonthOfEnd(dateTime);
        System.out.println("Month Of End: " + DateUtils.format(monthOfEnd));
        DateTime yearOfStart = DateUtils.getYearOfStart(dateTime);
        System.out.println("Year Of Start: " + DateUtils.format(yearOfStart));
        DateTime yearOfEnd = DateUtils.getYearOfEnd(dateTime);
        System.out.println("Year Of End: " + DateUtils.format(yearOfEnd));
        DateTime weekOfStart = DateUtils.getWeekOfStart(dateTime, 1);
        System.out.println("Week Of Start: " + DateUtils.format(weekOfStart));
        DateTime weekOfEnd = DateUtils.getWeekOfEnd(dateTime, 1);
        System.out.println("Week Of End: " + DateUtils.format(weekOfEnd));
    }

}
