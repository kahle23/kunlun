/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.exception;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * The exception tools Test.
 * @author Kahle
 */
public class ExceptionUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtilsTest.class);

    @Test
    public void test1() {
        try {
            this.throwException1();
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }

    @Test
    public void test2() {
        try {
            this.throwException2();
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }

    private void throwException1() {
        try {
            throw new UncheckedException("throwException1 >> UncheckedException");
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private void throwException2() {
        try {
            throw new IOException("throwException2 >> IOException");
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
