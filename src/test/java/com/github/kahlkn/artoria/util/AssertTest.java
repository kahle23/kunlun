package com.github.kahlkn.artoria.util;

import com.github.kahlkn.artoria.exception.ExceptionUtils;
import org.junit.Test;

import java.util.logging.Logger;

public class AssertTest {
    private static Logger log = Logger.getLogger(AssertTest.class.getName());

    @Test
    public void testNotNull() {
        try {
            Assert.notNull(null, null);
        }
        catch (Exception e) {
            log.severe(ExceptionUtils.toString(e));
        }
    }

}
