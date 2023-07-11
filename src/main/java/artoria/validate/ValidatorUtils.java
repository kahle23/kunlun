package artoria.validate;

import artoria.data.validation.Validator;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * The validator tools.
 * @author Kahle
 */
@Deprecated // TODO: 2022/11/19 Deletable
public class ValidatorUtils {
    private static Logger log = LoggerFactory.getLogger(ValidatorUtils.class);

    public static void register(String name, Validator validator) {

        artoria.data.validation.ValidatorUtils.registerValidator(name, validator);
    }

    public static Validator deregister(String name) {
        artoria.data.validation.ValidatorUtils.deregisterValidator(name);
        return null;
    }

    public static Object validate(String name, Object target) {

        return artoria.data.validation.ValidatorUtils.validate(name, target);
    }

    public static boolean validateToBoolean(String name, Object target) {
        Object validate = validate(name, target);
        Assert.notNull(validate, "The validate result cannot be null. ");
        return (Boolean) validate;
    }

}
