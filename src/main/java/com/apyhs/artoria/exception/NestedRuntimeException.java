package com.apyhs.artoria.exception;

/**
 * Nested runtime exception.
 * @author Kahle
 */
public abstract class NestedRuntimeException extends RuntimeException {

    public NestedRuntimeException() {
        super();
    }

    public NestedRuntimeException(String message) {
        super(message);
    }

    public NestedRuntimeException(Throwable cause) {
        super(cause);
    }

    public NestedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
