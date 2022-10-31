package artoria.data.validation;

/**
 * Provide the highest level of abstraction for validator.
 * @author Kahle
 */
public interface Validator {

    /**
     * The validate method of a target validator.
     * @param target The object that is to be validated
     * @return The result of validation or an exception thrown
     */
    Object validate(Object target);

}
