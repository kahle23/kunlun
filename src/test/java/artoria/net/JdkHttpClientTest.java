package artoria.net;

import artoria.codec.Base64Utils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static artoria.common.Constants.COLON;
import static artoria.common.Constants.DEFAULT_CHARSET_NAME;

@Ignore
public class JdkHttpClientTest {
    private static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    private static final String BASIC = "Basic ";
    private HttpClient httpClient = new JdkHttpClient();
    private String testUrl0 = "https://www.github.com";
    private String testUrl1 = "https://www.bing.com";

    @Test
    public void test1() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.GET);
        request.setUrl(testUrl1);
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusCode() + " | " + response.getStatusMessage());
        System.out.println(response.getBodyAsString());
    }

    @Test
    public void test2() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.GET);
        request.setUrl("http://ip.chinaz.com/getip.aspx");
        request.setProxy("127.0.0.1", 1080);
        request.addHeader(PROXY_AUTHORIZATION, BASIC +
                        Base64Utils.encodeToString(("admin" + COLON + "12345").getBytes(DEFAULT_CHARSET_NAME)));
        System.out.println(httpClient.execute(request).getBodyAsString());
    }

    @Test
    public void test3() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.GET);
        request.setUrl("https://raw.githubusercontent.com/torvalds/linux/master/README");
        // Choose one from three.
        request.setResponseBodyToFile(new File("e:\\README.txt"));
        request.setResponseBodyToStream(System.out);
        request.setResponseBodyToWriter(new OutputStreamWriter(System.out));
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusCode() + " | " + response.getStatusMessage());
        System.out.println(response.getBodyAsString());
    }

    @Test
    public void test4() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.POST);
        request.setUrl("https://");
        request.addParameter("file", new File("e:\\Temp\\2c6e0001b9bb631930b9.jpg"));
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusCode() + " | " + response.getStatusMessage());
        System.out.println(response.getBodyAsString());
    }

}
