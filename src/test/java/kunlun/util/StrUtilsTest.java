/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class StrUtilsTest {
    private static Logger log = LoggerFactory.getLogger(StrUtilsTest.class);

    @Test
    public void test1() {
        String str = "SAFASGF+ASIHFAS+OUAFHGA=";
        log.info(StrUtils.replace(str, "+", "-"));
    }

    @Test
    public void test2() {
        BigDecimal decimal = new BigDecimal("-3456");
        log.info("{}", decimal);
        BigDecimal decimal1 = new BigDecimal("+3456");
        log.info("{}", decimal1);
        log.info("{}", StrUtils.isNumeric("3452347"));
        log.info("{}", StrUtils.isNumeric("4373.4"));
        log.info("{}", StrUtils.isNumeric("345f347"));
        log.info("{}", StrUtils.isNumeric("-345347"));
        log.info("{}", StrUtils.isNumeric("-345."));
        log.info("{}", StrUtils.isNumeric("+0345.00.7"));
        log.info("{}", StrUtils.isNumeric("+3486.40"));
        log.info("{}", StrUtils.isNumeric("+3486..40"));
    }

    @Test
    public void test3() {
        log.info(StrUtils.camelToUnderline("helloWorld"));
        log.info(StrUtils.camelToUnderline("LocalHost"));
        log.info(StrUtils.camelToUnderline("localhost"));
        log.info(StrUtils.camelToUnderline("localhostVO"));
        log.info(StrUtils.camelToUnderline("local_host"));
        log.info(StrUtils.camelToUnderline("local_Host"));
        log.info(StrUtils.camelToUnderline("locaL_Host"));
        log.info(StrUtils.camelToUnderline("LOCALHOST"));
        log.info(StrUtils.camelToUnderline("HELLO_WORLD"));
        log.info(StrUtils.camelToUnderline("_LOCAL_HOST"));
        log.info(StrUtils.camelToUnderline(""));
        log.info(StrUtils.camelToUnderline(null));
        log.info("----");
        log.info(StrUtils.underlineToCamel("HELLO_WORLD"));
        log.info(StrUtils.underlineToCamel("_LOCAL_HOST"));
        log.info(StrUtils.underlineToCamel("LOCALHOST"));
        log.info(StrUtils.underlineToCamel("localhost"));
        log.info(StrUtils.underlineToCamel("local_host"));
        log.info(StrUtils.underlineToCamel("helloWorld"));
        log.info(StrUtils.underlineToCamel(""));
        log.info(StrUtils.underlineToCamel(null));
    }

    @Test
    public void test4() {
        String[] strArr = new String[]{"str1", "str2", "str3", "str4"};
        log.info(StrUtils.join(strArr, " - ", 1, 3));
        List<String> strList = Arrays.asList("str1", "str2", "str3", "str4");
        log.info(StrUtils.join(strList, " - ", 1, 3));
        String[] strArr1 = new String[]{"str1", "str2", "str3", "str4"};
        log.info(StrUtils.join(strArr1, " - "));
        List<String> strList1 = Arrays.asList("str1", "str2", "str3", "str4");
        log.info(StrUtils.join(strList1, " - "));
        log.info(StrUtils.join(new String[]{"str1 ", "str2 ", "str3 ", "str4 "}));
        log.info(StrUtils.join(new String[]{"str1 ", "str2 ", "str3 "}));
        log.info(StrUtils.join(new String[]{"str1 ", "str2 "}));
    }

}
