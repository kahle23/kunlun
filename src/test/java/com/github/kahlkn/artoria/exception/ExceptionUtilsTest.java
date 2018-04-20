package com.github.kahlkn.artoria.exception;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExceptionUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ExceptionUtilsTest.class);

    private void throwException1() throws Exception {
        try {
            throw new UncheckedException("throwException1");
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private void throwException2() throws Exception {
        try {
            throw new IOException("throwException1");
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e, UncheckedException.class);
        }
    }

    @Test
    public void test1() {
        try {
            throwException1();
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }

    @Test
    public void test2() {
        try {
            throwException2();
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }

}
