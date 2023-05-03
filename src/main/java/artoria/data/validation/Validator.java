package artoria.data.validation;

/**
 * Provide the highest level of abstraction for validator.
 * @author Kahle
 */
public interface Validator {

    /**
     * Validate that the target data meets expectations.
     * @param target The object that is to be validated
     * @return The result of validation or an exception thrown
     */
    Object validate(Object target);

}
