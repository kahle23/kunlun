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

import static org.junit.Assert.assertNotNull;

/**
 * The http tools Test.
 * @author Kahle
 */
@Ignore
public class HttpUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(HttpUtilsTest.class);

    @Test
    public void testExecute1() {
        String testUrl = "https://info.cern.ch";
        HttpResponse response = HttpUtils.execute(new SimpleRequest(testUrl, HttpMethod.GET));
        log.info(response.getBodyAsString());
        assertNotNull(response);
    }

    @Test
    public void testExecute2() {
        String testUrl = "http://www.gnu.org";
        HttpResponse response = HttpUtils.execute(new SimpleRequest(testUrl, HttpMethod.GET));
        log.info(response.getBodyAsString());
        assertNotNull(response);
    }

}
