package artoria.validate;

/**
 * Provide the highest level of abstraction for validator.
 * @author Kahle
 */
public interface Validator {

    /**
     * Return the validator name.
     * @return The validator name
     */
    String getName();

    /**
     * The validate method of a target validator.
     * @param target The object that is to be validated
     * @return The validation result
     */
    boolean validate(Object target);

}
