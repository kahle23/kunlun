package artoria.util;

import org.junit.Test;

import java.util.Properties;

public class PropertiesUtilsTest {

    @Test
    public void test1() {
        Properties properties = PropertiesUtils.create("logging.properties");
        properties.list(System.out);
    }

    @Test
    public void test2() {
        Properties properties = PropertiesUtils.create("jdbc.properties");
        properties.list(System.out);
    }

}
