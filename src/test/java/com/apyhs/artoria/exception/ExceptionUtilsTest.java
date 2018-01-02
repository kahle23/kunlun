package com.apyhs.artoria.exception;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import org.junit.Test;

public class ExceptionUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtilsTest.class);

    static {
        // Not register, also is GeneralException, in here, just test.
        ExceptionUtils.register(SystemCode.class, GeneralException.class);
    }

    @Test
    public void test1() {
        try {
            throw ExceptionUtils
                    .create(SystemCode.MEMORY_OVERFLOW)
                    .set("hello", "MEMORY 000000")
                    .set("address", "0364294");
        }
        catch (GeneralException e) {
            log.error(e.getMessage(), e);
        }
    }

}
