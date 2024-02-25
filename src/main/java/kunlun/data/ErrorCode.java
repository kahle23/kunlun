/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

/**
 * The error code information.
 * @see <a href="https://en.wikipedia.org/wiki/Error_code">Error code</a>
 * @author Kahle
 */
@Deprecated
public interface ErrorCode extends CodeDefinition {

    /**
     * Get the code of the error code.
     * @return The code of the error code
     */
    @Override
    String getCode();

}
