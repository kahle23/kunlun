package com.apyhs.artoria.util;

import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void test1() {
        System.out.println(NumberUtils.round(578.4000345f));
        System.out.println(NumberUtils.round(578.44325f) + 666);
        System.out.println(NumberUtils.round(578.478545f));
        System.out.println(NumberUtils.round(578.4455f));
    }

    @Test
    public void test2() {
        System.out.println(NumberUtils.format("00000", 99));
        System.out.println(NumberUtils.format(".000", 8754.65638));
        System.out.println(NumberUtils.format("0000000.000", 8754.65638));
        System.out.println(NumberUtils.format(".000", 8754.60000));
        System.out.println(NumberUtils.format("#######", 99));
        System.out.println(NumberUtils.format("#######.###", 8754.65638));
        System.out.println(NumberUtils.format("#######.##", 8754.60000));
    }

}
