package com.github.kahlkn.artoria.util;

import com.github.kahlkn.artoria.logging.Logger;
import com.github.kahlkn.artoria.logging.LoggerFactory;
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
