/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for loader.
 *
 * @author Kahle
 */
public interface Loader<Param, Result> {

    /**
     * Load the data based on parameters.
     *
     * @param param The parameters required for loading data
     * @return The loaded data
     */
    Result load(Param param);

}
