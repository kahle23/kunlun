/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net.http;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.net.http.support.SimpleRequest;
import org.junit.Ignore;
import org.junit.Test;

import static kunlun.net.http.HttpMethod.GET;
import static org.junit.Assert.assertNotNull;

/**
 * The http tools Test.
 * @author Kahle
 */
@Ignore
public class HttpUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(HttpUtilsTest.class);
    private static final String testUrl0 = "https://info.cern.ch";
    private static final String testUrl1 = "http://www.gnu.org";

    @Test
    public void testExecute1() {
        HttpResponse response = HttpUtils.execute(SimpleRequest.of(GET, testUrl0));
        log.info(response.getBodyAsString());
        assertNotNull(response);
    }

    @Test
    public void testExecute2() {
        HttpResponse response = HttpUtils.execute(SimpleRequest.of(GET, testUrl1));
        log.info(response.getBodyAsString());
        assertNotNull(response);
    }

    @Test
    public void testExecute3() {
        SimpleRequest request = SimpleRequest.of(GET, testUrl0);
        request.setStream(true);
        HttpResponse response = HttpUtils.execute(request);
        log.info(response.getBodyAsString());
        assertNotNull(response);
    }

}
