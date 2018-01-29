package com.apyhs.artoria.exception;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import org.junit.Test;

public class ExceptionUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ExceptionUtilsTest.class);

    @Test
    public void createAndSetOthers() {
        try {
            throw new BusinessException(SystemCode.MEMORY_OVERFLOW)
                    .setDescription("address in %s, say \"%s\". ", "0364294", "hello");
        }
        catch (BusinessException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void throwException1() throws BusinessException {
        try {
            throw new BusinessException(SystemCode.MEMORY_OVERFLOW)
                    .setDescription("[BusinessException]: Just Test...");
        }
        catch (BusinessException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private void throwException2() throws BusinessException {
        try {
            throw new UncheckedException(
                    "[UncheckedException]: Just Test...");
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Test
    public void testWrap1() {
        try {
            this.throwException1();
        }
        catch (BusinessException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testWrap2() {
        try {
            this.throwException2();
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}