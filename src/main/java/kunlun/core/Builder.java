/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for builder.
 *
 * When you need to use "Builder", you probably don't know its generic type.
 * So all you need to do is subclass override the return value of the method.
 * @author Kahle
 */
public interface Builder {

    /**
     * Build the target object.
     * @return The target object to be built
     */
    Object build();

}
