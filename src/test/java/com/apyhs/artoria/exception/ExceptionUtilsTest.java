package com.apyhs.artoria.exception;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import org.junit.Test;

public class ExceptionUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtilsTest.class);

    static {
        ExceptionUtils.register(SystemCode.class, SystemException.class);
    }

    @Test
    public void createAndSetOthers() {
        try {
            throw ExceptionUtils
                    .create(SystemCode.MEMORY_OVERFLOW)
                    .setOthers("address in %s, say \"%s\". ", "0364294", "hello");
        }
        catch (UncheckedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void check() {
        Integer i = (System.currentTimeMillis() >> 6) % 2 == 0 ? null : 1;
        try {
            ExceptionUtils.check(i != null
                    , SystemCode.ABNORMAL_SHUTDOWN
                    , "Hello, World! Test");
        }
        catch (UncheckedException e) {
            log.error(e.getMessage(), e);
        }
    }

}
