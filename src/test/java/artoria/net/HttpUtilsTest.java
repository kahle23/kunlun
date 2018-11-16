package artoria.net;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HttpUtilsTest {
    private static Logger log = LoggerFactory.getLogger(HttpUtilsTest.class);
    private String testUrl0 = "https://www.github.com";
    private String testUrl1 = "https://www.bing.com";

    @Test
    public void test1() {
        log.info(HttpUtils.get(testUrl0));
        log.info(HttpUtils.execute(testUrl0, HttpMethod.GET));
    }

    @Test
    public void test2() {
        log.info(HttpUtils.get(testUrl1));
        log.info(HttpUtils.execute(testUrl1, HttpMethod.GET));
    }

}
