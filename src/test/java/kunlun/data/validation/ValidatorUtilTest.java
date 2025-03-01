/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.validation;

import kunlun.data.validation.support.IsNumericValidator;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.ObjUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The validator tools Test.
 * @author Kahle
 */
public class ValidatorUtilTest {
    private static final Logger log = LoggerFactory.getLogger(ValidatorUtilTest.class);

    @Test
    public void testIsNumeric() {
        String name = "is_numeric"; Object result; Boolean result1;
        ValidatorUtil.registerValidator(name, new IsNumericValidator());

        result = ValidatorUtil.validate(name, "888.666");
        log.info("{}", result);
        assertTrue(ObjUtil.equals(result, true));

        result = ValidatorUtil.validate(name, "-888.666");
        log.info("{}", result);
        assertTrue(ObjUtil.equals(result, true));

        result1 = ValidatorUtil.validateToBoolean(name, "+888.666");
        log.info("{}", result1);
        assertEquals(result1, true);

        result1 = ValidatorUtil.validateToBoolean(name, "888.666w");
        log.info("{}", result1);
        assertEquals(result1, false);

        result1 = ValidatorUtil.validateToBoolean(name, "hello, world! ");
        log.info("{}", result1);
        assertEquals(result1, false);
    }

}
