/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support.util;

/**
 * The scope enumeration of data.
 * @author Kahle
 */
public enum DataScope {

    /**
     * Access to all data.
     */
    ALL,

    /**
     * Access only group data.
     */
    GROUP,

    /**
     * Access only self data.
     */
    SELF,

    /**
     * Can't access any data.
     */
    NONE,

    /**
     * The custom scope.
     */
    CUSTOM,

}
