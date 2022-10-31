package artoria.data.validation.support;

import artoria.data.validation.Validator;
import artoria.util.Assert;
import artoria.util.StringUtils;

/**
 * The numeric validator.
 * @author Kahle
 */
public class NumericValidator implements Validator {

    @Override
    public Boolean validate(Object target) {
        Assert.isInstanceOf(CharSequence.class, target
                , "The argument must be of type char sequence. ");
        return StringUtils.isNumeric((String) target);
    }

}
