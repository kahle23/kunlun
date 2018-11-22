package artoria.validate;

import artoria.exception.BusinessException;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static artoria.exception.SysErrorCode.PASSWORD_IS_REQUIRED;
import static artoria.exception.SysErrorCode.USERNAME_IS_REQUIRED;

public class ValidateUtilsTest {
    private Logger log = LoggerFactory.getLogger(ValidateUtilsTest.class);

    @Test
    public void test1() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            ValidateUtils.notEmpty(data, USERNAME_IS_REQUIRED);
        }
        catch (BusinessException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void test2() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            ValidateUtils.notEmpty(data, PASSWORD_IS_REQUIRED);
        }
        catch (BusinessException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void test3() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            ValidateUtils.notEmpty(data, null);
        }
        catch (BusinessException e) {
            log.error(e.getMessage(), e);
        }
    }

}
