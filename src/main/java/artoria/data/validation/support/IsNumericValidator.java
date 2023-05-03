package artoria.data.validation.support;

import artoria.data.validation.BooleanValidator;
import artoria.util.Assert;
import artoria.util.StringUtils;

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
