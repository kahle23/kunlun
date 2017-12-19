package artoria.util;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest {
    private static final Calendar TEMPLATE = Calendar.getInstance();

    @Test
    public void _performanceDateAndCalendar() {
        System.out.println();

        int count = 50; // 统计次数
        int createCount = 200000; // 每次创建的对象数

        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < createCount; j++) {
                Date date = new Date();
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();

        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < createCount; j++) {
                Calendar calendar = Calendar.getInstance();
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();

        // 克隆的性能略好于直接getInstance()
        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < createCount; j++) {
                Calendar calendar = (Calendar) TEMPLATE.clone();
                calendar.setTimeInMillis(System.currentTimeMillis());
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();

        System.out.println();
    }

    @Test
    public void _ifUnixTimestampUsingInteger() {
        System.out.println();
        System.out.println("If unix timestamp using Integer. ");
        DateUtils dateUtils = DateUtils.create(2038, 1, 23);
        System.out.println("The time of unix timestamp is " + dateUtils);
        System.out.println("The unix timestamp is " + dateUtils.getTimeInSeconds());
        System.out.println("The Integer max is " + Integer.MAX_VALUE);
        System.out.println();
    }

    @Test
    public void testEquals() {
        System.out.println();

        Date date1 = DateUtils.create(1000, 1, 1).getDate();
        Date date2 = DateUtils.create(1000, 1, 1).getDate();
        System.out.println("date1 equals date2 is true ? result : " + DateUtils.equals(date1, date2));

        Calendar calendar1 = DateUtils.create(2000, 1, 1).getCalendar();
        Calendar calendar2 = DateUtils.create(2000, 1, 1).getCalendar();
        System.out.println("calendar1 equals calendar2 is true ? result : " + DateUtils.equals(calendar1, calendar2));

        DateUtils dateUtils1 = DateUtils.create(1991, 11, 12);
        DateUtils dateUtils2 = DateUtils.create(1991, 11, 12);
        System.out.println("dateUtils1 equals dateUtils2 is true ? result : " + dateUtils1.equals(dateUtils2));

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
        System.out.println("The method toString(), result : " + DateUtils.create().toString());
        System.out.println("The method toString(String), result : " + DateUtils.create().toString(pattern));

        System.out.println();
    }

    @Test
    public void testCreateParse() throws ParseException {
        System.out.println();

        System.out.println("The date will using method format(Date), and default create method will create(). ");
        System.out.println("The method parse(Long), result : " + DateUtils.parse(DateUtils.create().getTimeInMillis()));
        System.out.println("The method parse(String), result : " + DateUtils.parse(DateUtils.create().toString()));
        System.out.println("The method parse(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result : "
                + DateUtils.format(DateUtils.parse(DateUtils.create().toString(), "yyyy-MM-dd HH:mm:ss SSS")));
        System.out.println("The method create(), result : " + DateUtils.create().toString());
        System.out.println("The method create(Date), result : " + DateUtils.create(new Date()).toString());
        System.out.println("The method create(Long), result : " + DateUtils.create(new Date().getTime()).toString());
        System.out.println("The method create(Calendar), result : " + DateUtils.create(Calendar.getInstance()));
        System.out.println("The method create(String), result : " + DateUtils.create(DateUtils.create().toString()));
        System.out.println("The method create(String, String), the pattern is \"yyyy-MM-dd HH:mm:ss SSS\", result : "
                + DateUtils.create(DateUtils.create().toString(), "yyyy-MM-dd HH:mm:ss SSS"));
        System.out.println("The method create(1990, 12, 12), result : " + DateUtils.create(1990, 12, 12));
        System.out.println("The method create(1990, 12, 12, 12, 12, 12), result : "
                + DateUtils.create(1990, 12, 12, 12, 12, 12));
        System.out.println("The method create(1990, 12, 12, 12, 12, 12, 12), result : "
                + DateUtils.create(1990, 12, 12, 12, 12, 12, 12));

        System.out.println();
    }

    @Test
    public void testTimestamp() {
        System.out.println();

        System.out.println("The method getTimestamp(), result : " + DateUtils.getTimestamp());
        System.out.println("The method getUnixTimestamp(), result : " + DateUtils.getUnixTimestamp());
        System.out.println("The method create().getTimeInMillis(), result : " + DateUtils.create().getTimeInMillis());
        System.out.println("The method create().getTimeInSeconds(), result : " + DateUtils.create().getTimeInSeconds());

        System.out.println();
    }

}
