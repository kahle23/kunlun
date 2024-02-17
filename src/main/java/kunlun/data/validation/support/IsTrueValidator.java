/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.validation.support;

import kunlun.common.constant.Words;
import kunlun.data.validation.BooleanValidator;

/**
 * The is true validator.
 * @author Kahle
 */
public class IsTrueValidator implements BooleanValidator {

    @Override
    public Boolean validate(Object target) {
        if (target instanceof Boolean) {
            return (Boolean) target;
        }
        else if (target instanceof String) {
            String str = (String) target;
            return Words.TRUE.equalsIgnoreCase(str);
        }
        else { return false; }
    }

}
