package artoria.data.validation.support;

import artoria.data.validation.BooleanValidator;
import artoria.util.ObjectUtils;

/**
 * The not blank validator.
 * @author Kahle
 */
public class NotEmptyValidator implements BooleanValidator {

    @Override
    public Boolean validate(Object target) {

        return !ObjectUtils.isEmpty(target);
    }

}
