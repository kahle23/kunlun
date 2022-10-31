package artoria.data.validation;

import artoria.data.validation.support.NumericValidator;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class ValidatorUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ValidatorUtilsTest.class);

    @Test
    public void testNumeric() {
        ValidatorUtils.register("numeric", new NumericValidator());
        log.info("{}", ValidatorUtils.validate("numeric", "888.666"));
        log.info("{}", ValidatorUtils.validate("numeric", "-888.666"));
        log.info("{}", ValidatorUtils.validateToBoolean("numeric", "+888.666"));
        log.info("{}", ValidatorUtils.validateToBoolean("numeric", "888.666w"));
        log.info("{}", ValidatorUtils.validateToBoolean("numeric", "hello, world! "));
    }

}
