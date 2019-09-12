package artoria.net;

import artoria.codec.Base64Utils;
import artoria.file.Text;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static artoria.common.Constants.*;

@Ignore
public class HttpClientTest {
    private static Logger log = LoggerFactory.getLogger(HttpClientTest.class);
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
        log.info("{} | {}", response.getStatusCode(), response.getStatusMessage());
        log.info(response.getBodyAsString());

        request.setUrl(testUrl1);
        response = httpClient.execute(request);
        log.info("{} | {}", response.getStatusCode(), response.getStatusMessage());
        log.info(response.getBodyAsString());
    }

    @Test
    public void test2() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.GET);
        request.setUrl("https://ip.chinaz.com/getip.aspx");
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
        log.info("{} | {}", response.getStatusCode(), response.getStatusMessage());
        log.info(response.getBodyAsString());
    }

    @Test
    public void test4() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.POST);
        request.setUrl("http://127.0.0.1:9988/test/upload");
        request.addParameter("file", new File("e:\\README.txt"));
        request.addParameter("name", "Hello");
        HttpResponse response = httpClient.execute(request);
        log.info("{} | {}", response.getStatusCode(), response.getStatusMessage());
        log.info(response.getBodyAsString());
    }

    @Test
    public void test5() throws IOException {
        HttpRequest request = new HttpRequest();
        request.setMethod(HttpMethod.POST);
        request.setUrl("http://127.0.0.1:9988/test/upload");
        Text text = new Text();
        text.setName("text1.txt");
        text.append("Test file entity upload. ").append(NEWLINE).append("Hello, World! ");
        request.addParameter("text1", text);
        text = new Text();
        text.setName("text2.txt");
        text.append("Test file entity upload by text2.txt. ").append(NEWLINE).append(">> Hello, World! ");
        request.addParameter("hello", "world");
        request.addParameter("text2", text);
        HttpResponse response = httpClient.execute(request);
        log.info("{} | {}", response.getStatusCode(), response.getStatusMessage());
        log.info(response.getBodyAsString());
    }

}
