package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.math.BigDecimal;

public class StringUtilsTest {
    private static Logger log = LoggerFactory.getLogger(StringUtilsTest.class);

    @Test
    public void test1() {
        String str = "SAFASGF+ASIHFAS+OUAFHGA=";
        log.info(StringUtils.replace(str, "+", "-"));
    }

    @Test
    public void test2() {
        BigDecimal decimal = new BigDecimal("-3456");
        log.info("{}", decimal);
        BigDecimal decimal1 = new BigDecimal("+3456");
        log.info("{}", decimal1);
        log.info("{}", StringUtils.isNumeric("3452347"));
        log.info("{}", StringUtils.isNumeric("4373.4"));
        log.info("{}", StringUtils.isNumeric("345f347"));
        log.info("{}", StringUtils.isNumeric("-345347"));
        log.info("{}", StringUtils.isNumeric("-345."));
        log.info("{}", StringUtils.isNumeric("+0345.00.7"));
        log.info("{}", StringUtils.isNumeric("+3486.40"));
        log.info("{}", StringUtils.isNumeric("+3486..40"));
    }

    @Test
    public void test3() {
        log.info(StringUtils.camelToUnderline("helloWorld"));
        log.info(StringUtils.camelToUnderline("LocalHost"));
        log.info(StringUtils.camelToUnderline("localhost"));
        log.info(StringUtils.camelToUnderline("localhostVO"));
        log.info(StringUtils.camelToUnderline("local_host"));
        log.info(StringUtils.camelToUnderline("local_Host"));
        log.info(StringUtils.camelToUnderline("locaL_Host"));
        log.info(StringUtils.camelToUnderline("LOCALHOST"));
        log.info(StringUtils.camelToUnderline("HELLO_WORLD"));
        log.info(StringUtils.camelToUnderline("_LOCAL_HOST"));
        log.info(StringUtils.camelToUnderline(""));
        log.info(StringUtils.camelToUnderline(null));
        log.info("----");
        log.info(StringUtils.underlineToCamel("HELLO_WORLD"));
        log.info(StringUtils.underlineToCamel("_LOCAL_HOST"));
        log.info(StringUtils.underlineToCamel("LOCALHOST"));
        log.info(StringUtils.underlineToCamel("localhost"));
        log.info(StringUtils.underlineToCamel("local_host"));
        log.info(StringUtils.underlineToCamel("helloWorld"));
        log.info(StringUtils.underlineToCamel(""));
        log.info(StringUtils.underlineToCamel(null));
    }

}
