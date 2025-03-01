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

public class DateTimeTest {
    private static Logger log = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    public void testIfUnixTimestampUsingInteger() {
        log.info("");
        log.info("If unix timestamp using Integer. ");
        DateTime dateTime = DateUtil.create(2038, ONE, TWENTY_THREE);
        log.info("The time of unix timestamp is {}", dateTime);
        log.info("The unix timestamp is {}", dateTime.getTimeInSeconds());
        log.info("The Integer max is {}", Integer.MAX_VALUE);
        log.info("");
    }

    @Test
    public void testDayOfAndWeekOf() {
        DateTime dateTime = DateUtil.create();
//        dateTime.addMonth(-1).addDay(-1);
//        dateTime.addDayOfWeek(1);
//        dateTime.setDayOfYear(365);
        log.info(DateUtil.format(dateTime));
        log.info(DateUtil.format(dateTime, "EEEE"));
        log.info("Day Of Week: {}", dateTime.getDayOfWeek());
        log.info("Day Of Week In Month: {}", dateTime.getDayOfWeekInMonth());
        log.info("Week Of Month: {}", dateTime.getWeekOfMonth());
        log.info("Week Of Year: {}", dateTime.getWeekOfYear());
        log.info("Day Of Year: {}", dateTime.getDayOfYear());
    }

    @Test
    public void testEquals() {
        log.info("");

        DateTime dateTime1 = DateUtil.create(1991, ELEVEN, TWELVE);
        DateTime dateTime2 = DateUtil.create(1991, ELEVEN, TWELVE);
        log.info("dateUtils1 equals dateUtils2 is true ? result: {}", dateTime1.equals(dateTime2));

        log.info("");
    }

    @Test
    public void testFormat() {
        log.info("");

        log.info("The method toString(), result: {}", DateUtil.create().toString());

        log.info("");
    }

    @Test
    public void testCreate() {
        log.info("");

        log.info("The method create(), result: {}", DateUtil.create().toString());
        log.info("The method create(Date), result: {}", DateUtil.create(new Date()).toString());
        log.info("The method create(Long), result: {}", DateUtil.create(new Date().getTime()).toString());
        log.info("The method create(Calendar), result: {}", DateUtil.create(Calendar.getInstance()));
        log.info("The method create(String), result: {}", DateUtil.create(DateUtil.create().toString()));
        log.info("The method create(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result: {}"
                , DateUtil.create(DateUtil.create().toString(), NORM_DATETIME));
        log.info("The method create(1990, 12, 12), result: {}", DateUtil.create(1990, TWELVE, TWELVE));
        log.info("The method create(1990, 12, 12, 12, 12, 12), result: {}"
                , DateUtil.create(1990, TWELVE, TWELVE, TWELVE, TWELVE, TWELVE));
        log.info("The method create(1990, 12, 12, 12, 12, 12, 12), result: {}"
                , DateUtil.create(1990, TWELVE, TWELVE, TWELVE, TWELVE, TWELVE, TWELVE));
        log.info("");
    }

    @Test
    public void testTimestamp() {
        log.info("");

        log.info("The method create().getTimeInMillis(), result: {}", DateUtil.create().getTimeInMillis());
        log.info("The method create().getTimeInSeconds(), result: {}", DateUtil.create().getTimeInSeconds());

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
