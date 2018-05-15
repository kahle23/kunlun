package com.github.kahlkn.artoria.exception;

import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

public class ExceptionUtilsTest {
    private static Logger log = Logger.getLogger(ExceptionUtilsTest.class.getName());

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
            log.info(ExceptionUtils.toString(e));
        }
    }

    @Test
    public void test2() {
        try {
            throwException2();
        }
        catch (Exception e) {
            log.info(ExceptionUtils.toString(e));
        }
    }

}
