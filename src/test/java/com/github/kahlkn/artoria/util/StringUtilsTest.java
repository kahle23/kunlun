package com.github.kahlkn.artoria.util;

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
        System.out.println(StringUtils.isNumeric("4373.4"));
        System.out.println(StringUtils.isNumeric("345f347"));
        System.out.println(StringUtils.isNumeric("-345347"));
        System.out.println(StringUtils.isNumeric("-345."));
        System.out.println(StringUtils.isNumeric("+0345.00.7"));
        System.out.println(StringUtils.isNumeric("+3486.40"));
        System.out.println(StringUtils.isNumeric("+3486..40"));
    }

    @Test
    public void test3() {
        System.out.println(StringUtils.camelToUnderline("helloWorld"));
        System.out.println(StringUtils.camelToUnderline("LocalHost"));
        System.out.println(StringUtils.camelToUnderline("localhost"));
        System.out.println(StringUtils.camelToUnderline("localhostVO"));
        System.out.println(StringUtils.camelToUnderline("local_host"));
        System.out.println(StringUtils.camelToUnderline("local_Host"));
        System.out.println(StringUtils.camelToUnderline("locaL_Host"));
        System.out.println(StringUtils.camelToUnderline("LOCALHOST"));
        System.out.println(StringUtils.camelToUnderline("HELLO_WORLD"));
        System.out.println(StringUtils.camelToUnderline("_LOCAL_HOST"));
        System.out.println(StringUtils.camelToUnderline(""));
        System.out.println(StringUtils.camelToUnderline(null));
        System.out.println("----");
        System.out.println(StringUtils.underlineToCamel("HELLO_WORLD"));
        System.out.println(StringUtils.underlineToCamel("_LOCAL_HOST"));
        System.out.println(StringUtils.underlineToCamel("LOCALHOST"));
        System.out.println(StringUtils.underlineToCamel("localhost"));
        System.out.println(StringUtils.underlineToCamel("local_host"));
        System.out.println(StringUtils.underlineToCamel("helloWorld"));
        System.out.println(StringUtils.underlineToCamel(""));
        System.out.println(StringUtils.underlineToCamel(null));
    }

}
