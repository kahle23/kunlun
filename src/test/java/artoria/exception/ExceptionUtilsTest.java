package artoria.exception;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;

public class ExceptionUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ExceptionUtilsTest.class);

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
            throw ExceptionUtils.wrap(e, UncheckedException.class);
        }
    }

}
