package artoria.data.validation;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The validator tools.
 * @author Kahle
 */
public class ValidatorUtils {
    private static final Map<String, Validator> VALIDATORS = new ConcurrentHashMap<String, Validator>();
    private static Logger log = LoggerFactory.getLogger(ValidatorUtils.class);

    public static void register(String name, Validator validator) {
        Assert.notNull(validator, "Parameter \"validator\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = validator.getClass().getName();
        log.info("Register \"{}\" to \"{}\". ", className, name);
        VALIDATORS.put(name, validator);
    }

    public static Validator deregister(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Validator remove = VALIDATORS.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister \"{}\" to \"{}\". ", className, name);
        }
        return remove;
    }

    public static Object validate(String name, Object target) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Validator validator = VALIDATORS.get(name);
        Assert.state(validator != null,
                "The validator named \"" + name + "\" could not be found. ");
        return validator.validate(target);
    }

    public static boolean validateToBoolean(String name, Object target) {
        Object validate = validate(name, target);
        Assert.notNull(validate, "The validate result cannot be null. ");
        return (Boolean) validate;
    }

}
