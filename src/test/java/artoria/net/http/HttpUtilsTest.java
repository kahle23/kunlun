package artoria.net.http;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.net.http.support.SimpleRequest;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * The http tools test class.
 * @author Kahle
 */
@Ignore
public class HttpUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(HttpUtilsTest.class);

    @Test
    public void testExecute1() {
        String testUrl = "https://www.github.com";
        HttpResponse response = HttpUtils.execute(new SimpleRequest(testUrl, HttpMethod.GET));
        log.info(response.getBodyAsString());
        assertNotNull(response);
    }

    @Test
    public void testExecute2() {
        String testUrl = "https://www.bing.com";
        HttpResponse response = HttpUtils.execute(new SimpleRequest(testUrl, HttpMethod.GET));
        log.info(response.getBodyAsString());
        assertNotNull(response);
    }

}
