/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.validation;

/**
 * Provide the highest level of abstraction for boolean validator.
 * @author Kahle
 */
public interface BooleanValidator extends Validator {

    /**
     * Validate that the target data meets expectations.
     * @param target The object that is to be validated
     * @return The boolean result of validation
     */
    @Override
    Boolean validate(Object target);

}
