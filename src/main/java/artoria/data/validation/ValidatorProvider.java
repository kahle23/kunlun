package artoria.data.validation;

import java.util.Map;

/**
 * The validator provider.
 * @author Kahle
 */
public interface ValidatorProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the validator.
     * @param name The validator name
     * @param validator The validator
     */
    void registerValidator(String name, Validator validator);

    /**
     * Deregister the validator.
     * @param name The validator name
     */
    void deregisterValidator(String name);

    /**
     * Get the validator by name.
     * @param name The validator name
     * @return The validator
     */
    Validator getValidator(String name);

    /**
     * Validate that the target data meets expectations.
     * @param name The validator name
     * @param target The object that is to be validated
     * @return The result of validation or an exception thrown
     */
    Object validate(String name, Object target);

    /**
     * Validate that the target data meets expectations (boolean result).
     * For general purposes, the Validator type is not checked to be BooleanValidator.
     * @param name The validator name
     * @param target The object that is to be validated
     * @return The result of validation
     * @see artoria.data.validation.BooleanValidator
     */
    boolean validateToBoolean(String name, Object target);

}
