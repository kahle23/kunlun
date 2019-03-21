package artoria.time;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateTimeTest {
    private static Logger log = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    public void testIfUnixTimestampUsingInteger() {
        log.info("");
        log.info("If unix timestamp using Integer. ");
        DateTime dateTime = DateUtils.create(2038, 1, 23);
        log.info("The time of unix timestamp is {}", dateTime);
        log.info("The unix timestamp is {}", dateTime.getTimeInSeconds());
        log.info("The Integer max is {}", Integer.MAX_VALUE);
        log.info("");
    }

    @Test
    public void testDayOfAndWeekOf() {
        DateTime dateTime = DateUtils.create();
//        dateTime.addMonth(-1).addDay(-1);
//        dateTime.addDayOfWeek(1);
//        dateTime.setDayOfYear(365);
        log.info(DateUtils.format(dateTime));
        log.info(DateUtils.format(dateTime, "EEEE"));
        log.info("Day Of Week: {}", dateTime.getDayOfWeek());
        log.info("Day Of Week In Month: {}", dateTime.getDayOfWeekInMonth());
        log.info("Week Of Month: {}", dateTime.getWeekOfMonth());
        log.info("Week Of Year: {}", dateTime.getWeekOfYear());
        log.info("Day Of Year: {}", dateTime.getDayOfYear());
    }

    @Test
    public void testEquals() {
        log.info("");

        DateTime dateTime1 = DateUtils.create(1991, 11, 12);
        DateTime dateTime2 = DateUtils.create(1991, 11, 12);
        log.info("dateUtils1 equals dateUtils2 is true ? result: {}", dateTime1.equals(dateTime2));

        log.info("");
    }

    @Test
    public void testFormat() {
        log.info("");

        log.info("The method toString(), result: {}", DateUtils.create().toString());

        log.info("");
    }

    @Test
    public void testCreate() throws ParseException {
        log.info("");

        log.info("The method create(), result: {}", DateUtils.create().toString());
        log.info("The method create(Date), result: {}", DateUtils.create(new Date()).toString());
        log.info("The method create(Long), result: {}", DateUtils.create(new Date().getTime()).toString());
        log.info("The method create(Calendar), result: {}", DateUtils.create(Calendar.getInstance()));
        log.info("The method create(String), result: {}", DateUtils.create(DateUtils.create().toString()));
        log.info("The method create(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result: {}"
                , DateUtils.create(DateUtils.create().toString(), "yyyy-MM-dd HH:mm:ss SSS"));
        log.info("The method create(1990, 12, 12), result: {}", DateUtils.create(1990, 12, 12));
        log.info("The method create(1990, 12, 12, 12, 12, 12), result: {}"
                , DateUtils.create(1990, 12, 12, 12, 12, 12));
        log.info("The method create(1990, 12, 12, 12, 12, 12, 12), result: {}"
                , DateUtils.create(1990, 12, 12, 12, 12, 12, 12));
        log.info("");
    }

    @Test
    public void testTimestamp() {
        log.info("");

        log.info("The method create().getTimeInMillis(), result: {}", DateUtils.create().getTimeInMillis());
        log.info("The method create().getTimeInSeconds(), result: {}", DateUtils.create().getTimeInSeconds());

        log.info("");
    }

    @Test
    public void testNew() {
        SimpleDateTime dateTime = new SimpleDateTime();
        log.info("{}", dateTime);
        SimpleDateTime dateTime1 = new SimpleDateTime(Calendar.getInstance());
        log.info("{}", dateTime1);
        SimpleDateTime dateTime2 = new SimpleDateTime(new Date());
        log.info("{}", dateTime2);
    }

}
