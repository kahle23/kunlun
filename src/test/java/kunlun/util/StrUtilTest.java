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

public class StrUtilTest {
    private static Logger log = LoggerFactory.getLogger(StrUtilTest.class);

    @Test
    public void test1() {
        String str = "SAFASGF+ASIHFAS+OUAFHGA=";
        log.info(StrUtil.replace(str, "+", "-"));
    }

    @Test
    public void test2() {
        BigDecimal decimal = new BigDecimal("-3456");
        log.info("{}", decimal);
        BigDecimal decimal1 = new BigDecimal("+3456");
        log.info("{}", decimal1);
        log.info("{}", StrUtil.isNumeric("3452347"));
        log.info("{}", StrUtil.isNumeric("4373.4"));
        log.info("{}", StrUtil.isNumeric("345f347"));
        log.info("{}", StrUtil.isNumeric("-345347"));
        log.info("{}", StrUtil.isNumeric("-345."));
        log.info("{}", StrUtil.isNumeric("+0345.00.7"));
        log.info("{}", StrUtil.isNumeric("+3486.40"));
        log.info("{}", StrUtil.isNumeric("+3486..40"));
    }

    @Test
    public void test3() {
        log.info(StrUtil.camelToUnderline("helloWorld"));
        log.info(StrUtil.camelToUnderline("LocalHost"));
        log.info(StrUtil.camelToUnderline("localhost"));
        log.info(StrUtil.camelToUnderline("localhostVO"));
        log.info(StrUtil.camelToUnderline("local_host"));
        log.info(StrUtil.camelToUnderline("local_Host"));
        log.info(StrUtil.camelToUnderline("locaL_Host"));
        log.info(StrUtil.camelToUnderline("LOCALHOST"));
        log.info(StrUtil.camelToUnderline("HELLO_WORLD"));
        log.info(StrUtil.camelToUnderline("_LOCAL_HOST"));
        log.info(StrUtil.camelToUnderline(""));
        log.info(StrUtil.camelToUnderline(null));
        log.info("----");
        log.info(StrUtil.underlineToCamel("HELLO_WORLD"));
        log.info(StrUtil.underlineToCamel("_LOCAL_HOST"));
        log.info(StrUtil.underlineToCamel("LOCALHOST"));
        log.info(StrUtil.underlineToCamel("localhost"));
        log.info(StrUtil.underlineToCamel("local_host"));
        log.info(StrUtil.underlineToCamel("helloWorld"));
        log.info(StrUtil.underlineToCamel(""));
        log.info(StrUtil.underlineToCamel(null));
    }

    @Test
    public void test4() {
        String[] strArr = new String[]{"str1", "str2", "str3", "str4"};
        log.info(StrUtil.join(strArr, " - ", 1, 3));
        List<String> strList = Arrays.asList("str1", "str2", "str3", "str4");
        log.info(StrUtil.join(strList, " - ", 1, 3));
        String[] strArr1 = new String[]{"str1", "str2", "str3", "str4"};
        log.info(StrUtil.join(strArr1, " - "));
        List<String> strList1 = Arrays.asList("str1", "str2", "str3", "str4");
        log.info(StrUtil.join(strList1, " - "));
        log.info(StrUtil.join(new String[]{"str1 ", "str2 ", "str3 ", "str4 "}));
        log.info(StrUtil.join(new String[]{"str1 ", "str2 ", "str3 "}));
        log.info(StrUtil.join(new String[]{"str1 ", "str2 "}));
    }

}
