package artoria.time;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static artoria.common.Constants.*;

public class DateUtilsTest {
    private static Logger log = LoggerFactory.getLogger(DateUtilsTest.class);

    @Test
    public void testEquals() {
        log.info("");

        Date date1 = DateUtils.create(ONE_THOUSAND, ONE, ONE).getDate();
        Date date2 = DateUtils.create(ONE_THOUSAND, ONE, ONE).getDate();
        log.info("date1 equals date2 is true ? result: {}", DateUtils.equals(date1, date2));

        Calendar calendar1 = DateUtils.create(TWO_HUNDRED, ONE, ONE).getCalendar();
        Calendar calendar2 = DateUtils.create(TWO_HUNDRED, ONE, ONE).getCalendar();
        log.info("calendar1 equals calendar2 is true ? result: {}", DateUtils.equals(calendar1, calendar2));

        log.info("");
    }

    @Test
    public void testFormat() {
        log.info("");

        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
//        String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//        String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        log.info("Test format, The date will new, The pattern is \"{}\". ", pattern);
        log.info("The method format(), result: {}", DateUtils.format());
        log.info("The method format(Long), result: {}", DateUtils.format(System.currentTimeMillis()));
        log.info("The method format(String), result: {}", DateUtils.format(pattern));
        log.info("The method format(Date), result: {}", DateUtils.format(new Date()));
        log.info("The method format(Long, String), result: {}", DateUtils.format(new Date().getTime(), pattern));
        log.info("The method format(Date, String), result: {}", DateUtils.format(new Date(), pattern));

        log.info("");
    }

    @Test
    public void testParseFormat() {
        log.info("");

        log.info("The method parse(Long), result: {}", DateUtils.parse(DateUtils.create().getTimeInMillis()));
        log.info("The method parse(String), result: {}", DateUtils.parse(DateUtils.create().toString()));
        log.info("The method parse(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result: {}"
                , DateUtils.format(DateUtils.parse(DateUtils.create().toString(), "yyyy-MM-dd HH:mm:ss SSS")));

        log.info("");
    }

    @Test
    public void testTimestamp() {
        log.info("");

        log.info("The method getTimeInMillis(), result: {}", DateUtils.getTimeInMillis());
        log.info("The method getTimeInSeconds(), result: {}", DateUtils.getTimeInSeconds());

        log.info("");
    }

    @Test
    public void testAddAmount() {
        Date date = new Date();
        log.info(DateUtils.format(DateUtils.addYear(date, TEN)));
        log.info(DateUtils.format(DateUtils.addMonth(date, TEN)));
        log.info(DateUtils.format(DateUtils.addDay(date, TEN)));
        log.info(DateUtils.format(DateUtils.addHour(date, TEN)));
        log.info(DateUtils.format(DateUtils.addMinute(date, TEN)));
        log.info(DateUtils.format(DateUtils.addSecond(date, TEN)));
        log.info(DateUtils.format(DateUtils.addMillisecond(date, TEN)));
    }

    @Test
    public void testGetAmount() {
        Date date = new Date();
        log.info("{}", DateUtils.getYear(date));
        log.info("{}", DateUtils.getMonth(date));
        log.info("{}", DateUtils.getDay(date));
        log.info("{}", DateUtils.getHour(date));
        log.info("{}", DateUtils.getMinute(date));
        log.info("{}", DateUtils.getSecond(date));
        log.info("{}", DateUtils.getMillisecond(date));
    }

    @Test
    public void testSetAmount() {
        Date date = new Date();
        log.info(DateUtils.format(DateUtils.setYear(date, TEN)));
        log.info(DateUtils.format(DateUtils.setMonth(date, TEN)));
        log.info(DateUtils.format(DateUtils.setDay(date, TEN)));
        log.info(DateUtils.format(DateUtils.setHour(date, TEN)));
        log.info(DateUtils.format(DateUtils.setMinute(date, TEN)));
        log.info(DateUtils.format(DateUtils.setSecond(date, TEN)));
        log.info(DateUtils.format(DateUtils.setMillisecond(date, TEN)));
    }

    @Test
    public void testOfStartOfEnd() {
        DateTime dateTime = DateUtils.create();
        log.info("Now time: {}", DateUtils.format(dateTime));
        DateTime dayOfStart = DateUtils.getDayOfStart(dateTime);
        log.info("Day Of Start: {}", DateUtils.format(dayOfStart));
        DateTime dayOfEnd = DateUtils.getDayOfEnd(dateTime);
        log.info("Day Of End: {}", DateUtils.format(dayOfEnd));
        DateTime monthOfStart = DateUtils.getMonthOfStart(dateTime);
        log.info("Month Of Start: {}", DateUtils.format(monthOfStart));
        DateTime monthOfEnd = DateUtils.getMonthOfEnd(dateTime);
        log.info("Month Of End: {}", DateUtils.format(monthOfEnd));
        DateTime yearOfStart = DateUtils.getYearOfStart(dateTime);
        log.info("Year Of Start: {}", DateUtils.format(yearOfStart));
        DateTime yearOfEnd = DateUtils.getYearOfEnd(dateTime);
        log.info("Year Of End: {}", DateUtils.format(yearOfEnd));
        DateTime weekOfStart = DateUtils.getWeekOfStart(dateTime, ONE);
        log.info("Week Of Start: {}", DateUtils.format(weekOfStart));
        DateTime weekOfEnd = DateUtils.getWeekOfEnd(dateTime, ONE);
        log.info("Week Of End: {}", DateUtils.format(weekOfEnd));
    }

}
