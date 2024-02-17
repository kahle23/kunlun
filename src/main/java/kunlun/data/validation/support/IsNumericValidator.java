/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.validation.support;

import kunlun.data.validation.BooleanValidator;
import kunlun.util.Assert;
import kunlun.util.StringUtils;

/**
 * The is numeric validator.
 * @author Kahle
 */
public class IsNumericValidator implements BooleanValidator {

    @Override
    public Boolean validate(Object target) {
        Assert.isInstanceOf(String.class, target
                , "The argument must be of type string. ");
        return StringUtils.isNumeric((String) target);
    }

}
