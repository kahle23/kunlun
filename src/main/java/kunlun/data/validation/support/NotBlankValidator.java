/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.validation.support;

import kunlun.data.validation.BooleanValidator;
import kunlun.util.Assert;
import kunlun.util.StringUtils;

/**
 * The not blank validator.
 * @author Kahle
 */
public class NotBlankValidator implements BooleanValidator {

    @Override
    public Boolean validate(Object target) {
        if (target == null) { return false; }
        Assert.isInstanceOf(CharSequence.class, target
                , "The argument must be of type char sequence. ");
        return StringUtils.isNotBlank((CharSequence) target);
    }

}
