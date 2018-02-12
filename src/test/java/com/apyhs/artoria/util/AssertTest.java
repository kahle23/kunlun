package com.apyhs.artoria.util;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import org.junit.Test;

public class AssertTest {
    private static Logger log = LoggerFactory.getLogger(AssertTest.class);

    @Test
    public void testNotNull() {
        try {
            Assert.notNull(null, null);
        }
        catch (Exception e) {
            log.error(e);
        }
    }

}
