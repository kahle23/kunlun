package artoria.data.validation;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * The validator tools.
 * @author Kahle
 */
public class ValidatorUtils {
    private static final Logger log = LoggerFactory.getLogger(ValidatorUtils.class);
    private static volatile ValidatorProvider validatorProvider;

    public static ValidatorProvider getValidatorProvider() {
        if (validatorProvider != null) { return validatorProvider; }
        synchronized (ValidatorUtils.class) {
            if (validatorProvider != null) { return validatorProvider; }
            ValidatorUtils.setValidatorProvider(new SimpleValidatorProvider());
            return validatorProvider;
        }
    }

    public static void setValidatorProvider(ValidatorProvider validatorProvider) {
        Assert.notNull(validatorProvider, "Parameter \"validatorProvider\" must not null. ");
        log.info("Set validator provider: {}", validatorProvider.getClass().getName());
        ValidatorUtils.validatorProvider = validatorProvider;
    }

    public static void registerValidator(String name, Validator validator) {

        getValidatorProvider().registerValidator(name, validator);
    }

    public static void deregisterValidator(String name) {

        getValidatorProvider().deregisterValidator(name);
    }

    public static Validator getValidator(String name) {

        return getValidatorProvider().getValidator(name);
    }

    public static Object validate(String name, Object target) {

        return getValidatorProvider().validate(name, target);
    }

    public static boolean validateToBoolean(String name, Object target) {

        return getValidatorProvider().validateToBoolean(name, target);
    }

}
