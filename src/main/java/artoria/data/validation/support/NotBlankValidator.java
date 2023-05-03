package artoria.data.validation.support;

import artoria.data.validation.BooleanValidator;
import artoria.util.Assert;
import artoria.util.StringUtils;

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
