package artoria.data.validation;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple validator provider.
 * @author Kahle
 */
public class SimpleValidatorProvider implements ValidatorProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleValidatorProvider.class);
    protected final Map<String, Validator> validators;
    protected final Map<String, Object> commonProperties;

    protected SimpleValidatorProvider(Map<String, Object> commonProperties,
                                      Map<String, Validator> validators) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(validators, "Parameter \"validators\" must not null. ");
        this.commonProperties = commonProperties;
        this.validators = validators;
    }

    public SimpleValidatorProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, Validator>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerValidator(String name, Validator validator) {
        Assert.notNull(validator, "Parameter \"validator\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = validator.getClass().getName();
        //validator.setCommonProperties(getCommonProperties())
        validators.put(name, validator);
        log.debug("Register the validator \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterValidator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Validator remove = validators.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the validator \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public Validator getValidator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Validator validator = validators.get(name);
        Assert.notNull(validator
                , "The corresponding validator could not be found by name. ");
        return validator;
    }

    @Override
    public Object validate(String name, Object target) {

        return getValidator(name).validate(target);
    }

    @Override
    public boolean validateToBoolean(String name, Object target) {
        Object validate = validate(name, target);
        Assert.notNull(validate, "The validate result cannot be null. ");
        return (Boolean) validate;
    }

}
