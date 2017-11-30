package artoriatest.util;

import org.junit.Test;
import artoria.util.Http;

public class HttpTest {

    @Test
    public void test1() throws Exception {
//        System.out.println(Http.on("http://uux.me").get());
//        System.out.println(Http.on("https://www.taobao.com").get());
        System.out.println(Http.on("https://www.baidu.com").get());
    }

    @Test
    public void test2() throws Exception {
        System.out.println(Http.on("http://uux.me").get());
//        System.out.println(Http.on("http://uux.me").post());
//        System.out.println(Http.on("http://uux.me").put());
//        System.out.println(Http.on("http://uux.me").delete());
//        System.out.println(Http.on("http://uux.me").head());
//        System.out.println(Http.on("http://uux.me").trace());
//        System.out.println(Http.on("http://uux.me").options());
//        System.out.println(Http.on("http://uux.me").setMethod("GET").send());
    }

    @Test
    public void test3() throws Exception {
        Http http = Http.on();
//        System.out.println(http.setUrl("http://uux.me").get());
//        System.out.println(http.setUrl("https://www.baidu.com").get());
        http.setConnectTimeout(1000).setReadTimeout(1000)
                .setUserAgent(">>>>>>>>>>>>>>>>>>>>>").setCharset("utf-8");
        System.out.println(http.setUrl("http://uux.me").get());
        System.out.println(http.setUrl("https://www.baidu.com").get());
    }

    @Test
    public void test4() throws Exception {
        Http http = Http.on().setConnectTimeout(1000)
                .setReadTimeout(1000).setCharset("utf-8")
                .setUserAgent(">>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(http.get("http://uux.me"));
//        System.out.println(http.post("http://uux.me"));
        System.out.println(http.send("http://uux.me", "GET"));
    }

    @Test
    public void test5() throws Exception {
        String url = "http://ip.chinaz.com/getip.aspx";
        System.out.println(Http.on(url)
                .setHttpProxy("127.0.0.1", 5000)
                .setProxyAuth("zhangsan", "123")
                .get());
    }

}
