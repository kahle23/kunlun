package com.apyhs.artoria.util;

import org.junit.Test;

import java.math.BigDecimal;

public class StringUtilsTest {

    @Test
    public void test1() {
        String str = "SAFASGF+ASIHFAS+OUAFHGA=";
        System.out.println(StringUtils.replace(str, "+", "-"));
    }

    @Test
    public void test2() {
        BigDecimal decimal = new BigDecimal("-3456");
        System.out.println(decimal);
        BigDecimal decimal1 = new BigDecimal("+3456");
        System.out.println(decimal1);
        System.out.println(StringUtils.isNumeric("3452347"));
        System.out.println(StringUtils.isNumeric("43734"));
        System.out.println(StringUtils.isNumeric("345f347"));
        System.out.println(StringUtils.isNumeric("-345347"));
        System.out.println(StringUtils.isNumeric("+345347"));
    }

}
