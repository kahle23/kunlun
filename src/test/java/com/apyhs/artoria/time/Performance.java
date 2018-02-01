package com.apyhs.artoria.time;

import com.apyhs.artoria.util.ThreadLocalUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Performance {
    private static final Calendar TEMPLATE = Calendar.getInstance();
    private int count = 50;
    private int createCount = 200000;

    @Test
    public void createDateAndCalendar() {
        System.out.println();

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
    public void createSimpleDateFormat() {
        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < createCount; j++) {
                new SimpleDateFormat(DateUtils.DEFAULT_DATE_PATTERN);
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();

        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < createCount; j++) {
                SimpleDateFormat dateFormat =
                        (SimpleDateFormat) ThreadLocalUtils.getValue(DateUtils.DEFAULT_DATE_PATTERN);
                if (dateFormat == null) {
                    dateFormat =
                            new SimpleDateFormat(DateUtils.DEFAULT_DATE_PATTERN);
                    ThreadLocalUtils.setValue(DateUtils.DEFAULT_DATE_PATTERN, dateFormat);
                }
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();
    }

}
