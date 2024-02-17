/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.validation;

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
