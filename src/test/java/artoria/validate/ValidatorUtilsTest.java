package artoria.validate;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class ValidatorUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ValidatorUtilsTest.class);

    @Test
    public void testNumeric() {
        ValidatorUtils.register(new NumericValidator("numeric"));
        log.info("{}", ValidatorUtils.validate("numeric", "888.666"));
        log.info("{}", ValidatorUtils.validate("numeric", "-888.666"));
        log.info("{}", ValidatorUtils.validate("numeric", "+888.666"));
        log.info("{}", ValidatorUtils.validate("numeric", "888.666w"));
        log.info("{}", ValidatorUtils.validate("numeric", "hello, world! "));
    }

}
