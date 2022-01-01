package artoria.validate;

import artoria.util.Assert;
import artoria.util.StringUtils;

/**
 * The numeric validator.
 * @author Kahle
 */
public class NumericValidator extends AbstractValidator {

    public NumericValidator(String name) {

        super(name);
    }

    @Override
    public boolean validate(Object target) {
        Assert.isInstanceOf(CharSequence.class, target
                , "The argument must be of type char sequence. ");
        return StringUtils.isNumeric((String) target);
    }

}
