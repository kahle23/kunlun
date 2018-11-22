package artoria.exception;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.exception.SysErrorCode.PASSWORD_IS_REQUIRED;
import static artoria.exception.SysErrorCode.USERNAME_IS_REQUIRED;

public class BusinessExceptionTest {
    private static Logger log = LoggerFactory.getLogger(BusinessExceptionTest.class);

    @Test
    public void testCreate() {
        try {
            throw new BusinessException(USERNAME_IS_REQUIRED);
        }
        catch (BusinessException e) {
            log.error(e.getMessage(), e);
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

    private void throwException1() {
        try {
            throw new BusinessException(PASSWORD_IS_REQUIRED);
        }
        catch (BusinessException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private void throwException2() {
        try {
            throw new BusinessException("[BusinessException]: Just Test...");
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
