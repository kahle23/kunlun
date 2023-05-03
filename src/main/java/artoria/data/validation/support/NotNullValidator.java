package artoria.data.validation.support;

import artoria.data.validation.BooleanValidator;

/**
 * The not null validator.
 * @author Kahle
 */
public class NotNullValidator implements BooleanValidator {

    @Override
    public Boolean validate(Object target) {

        return target != null;
    }

}
