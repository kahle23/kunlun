package artoria.util;

import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void test1() {
        String str = "SAFASGF+ASIHFAS+OUAFHGA=";
        System.out.println(StringUtils.replace(str, "+", "-"));
    }

}
