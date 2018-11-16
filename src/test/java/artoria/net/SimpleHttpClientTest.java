package artoria.net;

import artoria.codec.Base64Utils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static artoria.common.Constants.COLON;
import static artoria.common.Constants.DEFAULT_CHARSET_NAME;

@Ignore
public class SimpleHttpClientTest {
    private static Logger log = LoggerFactory.getLogger(SimpleHttpClientTest.class);
    private static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    private static final String BASIC = "Basic ";
    private HttpClient httpClient = new SimpleHttpClient();
    private String testUrl0 = "https://www.github.com";
    private String testUrl1 = "https://www.bing.com";

    @Test
    public void test1() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.GET);
        request.setUrl(testUrl0);
        HttpResponse response = httpClient.execute(request);
        log.info(response.getStatusCode() + " | " + response.getStatusMessage());
        log.info(response.getBodyAsString());

        request.setUrl(testUrl1);
        response = httpClient.execute(request);
        log.info(response.getStatusCode() + " | " + response.getStatusMessage());
        log.info(response.getBodyAsString());
    }

    @Test
    public void test2() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.GET);
        request.setUrl("http://ip.chinaz.com/getip.aspx");
        request.setProxy("127.0.0.1", 1080);
        request.addHeader(PROXY_AUTHORIZATION, BASIC +
                        Base64Utils.encodeToString(("admin" + COLON + "12345").getBytes(DEFAULT_CHARSET_NAME)));
        log.info(httpClient.execute(request).getBodyAsString());
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
        log.info(response.getStatusCode() + " | " + response.getStatusMessage());
        log.info(response.getBodyAsString());
    }

    @Test
    public void test4() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.POST);
        request.setUrl("https://");
        request.addParameter("file", new File("e:\\Temp\\2c6e0001b9bb631930b9.jpg"));
        HttpResponse response = httpClient.execute(request);
        log.info(response.getStatusCode() + " | " + response.getStatusMessage());
        log.info(response.getBodyAsString());
    }

}
