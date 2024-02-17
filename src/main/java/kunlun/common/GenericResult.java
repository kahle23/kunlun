/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

/**
 * The generic result.
 * @author Kahle
 */
@Deprecated
public interface GenericResult {

    /**
     * Get status code.
     * @return The status code
     */
    String getCode();

    /**
     * Set status code.
     * @param code The status code
     */
    void setCode(String code);

    /**
     * Get message content.
     * @return The message content
     */
    String getMessage();

    /**
     * Set message content.
     * @param message The message content
     */
    void setMessage(String message);

}
