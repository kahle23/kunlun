/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.exception;

/**
 * The unchecked exception.
 * @author Kahle
 */
public class UncheckedException extends RuntimeException {

    public UncheckedException() {

        super();
    }

    public UncheckedException(String message) {

        super(message);
    }

    public UncheckedException(Throwable cause) {

        super(cause);
    }

    public UncheckedException(String message, Throwable cause) {

        super(message, cause);
    }

}
