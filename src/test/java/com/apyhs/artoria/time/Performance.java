package com.apyhs.artoria.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class Performance {
    private static final Calendar TEMPLATE = Calendar.getInstance();

    @Test
    public void _performanceDateAndCalendar() {
        System.out.println();

        int count = 50;
        int createCount = 200000;

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

}
