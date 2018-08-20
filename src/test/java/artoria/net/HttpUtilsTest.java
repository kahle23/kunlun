package artoria.net;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HttpUtilsTest {
    private String testUrl0 = "https://www.github.com";
    private String testUrl1 = "https://www.bing.com";

    @Test
    public void test1() {
        System.out.println(HttpUtils.get(testUrl0));
        System.out.println(HttpUtils.execute(testUrl0, HttpMethod.GET));
    }

    @Test
    public void test2() {
        System.out.println(HttpUtils.get(testUrl1));
        System.out.println(HttpUtils.execute(testUrl1, HttpMethod.GET));
    }

}
