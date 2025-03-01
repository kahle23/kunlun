/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.time;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.TimePatterns.NORM_DATETIME;

public class DateUtilTest {
    private static Logger log = LoggerFactory.getLogger(DateUtilTest.class);

    @Test
    public void testEquals() {
        log.info("");

        Date date1 = DateUtil.create(ONE_THOUSAND, ONE, ONE).getDate();
        Date date2 = DateUtil.create(ONE_THOUSAND, ONE, ONE).getDate();
        log.info("date1 equals date2 is true ? result: {}", DateUtil.equals(date1, date2));

        Calendar calendar1 = DateUtil.create(TWO_HUNDRED, ONE, ONE).getCalendar();
        Calendar calendar2 = DateUtil.create(TWO_HUNDRED, ONE, ONE).getCalendar();
        log.info("calendar1 equals calendar2 is true ? result: {}", DateUtil.equals(calendar1, calendar2));

        log.info("");
    }

    @Test
    public void testFormat() {
        log.info("");

        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
//        String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//        String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        log.info("Test format, The date will new, The pattern is \"{}\". ", pattern);
        log.info("The method format(), result: {}", DateUtil.format());
        log.info("The method format(Long), result: {}", DateUtil.format(System.currentTimeMillis()));
        log.info("The method format(String), result: {}", DateUtil.format(pattern));
        log.info("The method format(Date), result: {}", DateUtil.format(new Date()));
        log.info("The method format(Long, String), result: {}", DateUtil.format(new Date().getTime(), pattern));
        log.info("The method format(Date, String), result: {}", DateUtil.format(new Date(), pattern));

        log.info("");
    }

    @Test
    public void testParseFormat() {
        log.info("");

        log.info("The method parse(Long), result: {}", DateUtil.parse(DateUtil.create().getTimeInMillis()));
        log.info("The method parse(String), result: {}", DateUtil.parse(DateUtil.create().toString()));
        log.info("The method parse(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result: {}"
                , DateUtil.format(DateUtil.parse(DateUtil.create().toString(), NORM_DATETIME)));

        log.info("");
    }

    @Test
    public void testTimestamp() {
        log.info("");

        log.info("The method getTimeInMillis(), result: {}", DateUtil.getTimeInMillis());
        log.info("The method getTimeInSeconds(), result: {}", DateUtil.getTimeInSeconds());

        log.info("");
    }

    @Test
    public void testAddAmount() {
        Date date = new Date();
        log.info(DateUtil.format(DateUtil.addYear(date, TEN)));
        log.info(DateUtil.format(DateUtil.addMonth(date, TEN)));
        log.info(DateUtil.format(DateUtil.addDay(date, TEN)));
        log.info(DateUtil.format(DateUtil.addHour(date, TEN)));
        log.info(DateUtil.format(DateUtil.addMinute(date, TEN)));
        log.info(DateUtil.format(DateUtil.addSecond(date, TEN)));
        log.info(DateUtil.format(DateUtil.addMillisecond(date, TEN)));
    }

    @Test
    public void testGetAmount() {
        Date date = new Date();
        log.info("{}", DateUtil.getYear(date));
        log.info("{}", DateUtil.getMonth(date));
        log.info("{}", DateUtil.getDay(date));
        log.info("{}", DateUtil.getHour(date));
        log.info("{}", DateUtil.getMinute(date));
        log.info("{}", DateUtil.getSecond(date));
        log.info("{}", DateUtil.getMillisecond(date));
    }

    @Test
    public void testSetAmount() {
        Date date = new Date();
        log.info(DateUtil.format(DateUtil.setYear(date, TEN)));
        log.info(DateUtil.format(DateUtil.setMonth(date, TEN)));
        log.info(DateUtil.format(DateUtil.setDay(date, TEN)));
        log.info(DateUtil.format(DateUtil.setHour(date, TEN)));
        log.info(DateUtil.format(DateUtil.setMinute(date, TEN)));
        log.info(DateUtil.format(DateUtil.setSecond(date, TEN)));
        log.info(DateUtil.format(DateUtil.setMillisecond(date, TEN)));
    }

    @Test
    public void testOfStartOfEnd() {
        DateTime dateTime = DateUtil.create();
        log.info("Now time: {}", DateUtil.format(dateTime));
        DateTime dayOfStart = DateUtil.getDayOfStart(dateTime);
        log.info("Day Of Start: {}", DateUtil.format(dayOfStart));
        DateTime dayOfEnd = DateUtil.getDayOfEnd(dateTime);
        log.info("Day Of End: {}", DateUtil.format(dayOfEnd));
        DateTime monthOfStart = DateUtil.getMonthOfStart(dateTime);
        log.info("Month Of Start: {}", DateUtil.format(monthOfStart));
        DateTime monthOfEnd = DateUtil.getMonthOfEnd(dateTime);
        log.info("Month Of End: {}", DateUtil.format(monthOfEnd));
        DateTime yearOfStart = DateUtil.getYearOfStart(dateTime);
        log.info("Year Of Start: {}", DateUtil.format(yearOfStart));
        DateTime yearOfEnd = DateUtil.getYearOfEnd(dateTime);
        log.info("Year Of End: {}", DateUtil.format(yearOfEnd));
        DateTime weekOfStart = DateUtil.getWeekOfStart(dateTime, ONE);
        log.info("Week Of Start: {}", DateUtil.format(weekOfStart));
        DateTime weekOfEnd = DateUtil.getWeekOfEnd(dateTime, ONE);
        log.info("Week Of End: {}", DateUtil.format(weekOfEnd));
    }

}
