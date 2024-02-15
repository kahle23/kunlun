package artoria.data.validation.support;

import artoria.common.constant.Words;
import artoria.data.validation.BooleanValidator;

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
