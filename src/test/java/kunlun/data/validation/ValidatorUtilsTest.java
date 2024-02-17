/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.validation;

import kunlun.data.validation.support.IsNumericValidator;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.ObjectUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The validator tools Test.
 * @author Kahle
 */
public class ValidatorUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ValidatorUtilsTest.class);

    @Test
    public void testIsNumeric() {
        String name = "is_numeric"; Object result; Boolean result1;
        ValidatorUtils.registerValidator(name, new IsNumericValidator());

        result = ValidatorUtils.validate(name, "888.666");
        log.info("{}", result);
        assertTrue(ObjectUtils.equals(result, true));

        result = ValidatorUtils.validate(name, "-888.666");
        log.info("{}", result);
        assertTrue(ObjectUtils.equals(result, true));

        result1 = ValidatorUtils.validateToBoolean(name, "+888.666");
        log.info("{}", result1);
        assertEquals(result1, true);

        result1 = ValidatorUtils.validateToBoolean(name, "888.666w");
        log.info("{}", result1);
        assertEquals(result1, false);

        result1 = ValidatorUtils.validateToBoolean(name, "hello, world! ");
        log.info("{}", result1);
        assertEquals(result1, false);
    }

}
