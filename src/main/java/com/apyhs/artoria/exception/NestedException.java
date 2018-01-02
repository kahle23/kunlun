package com.apyhs.artoria.exception;

/**
 * Nested exception.
 * @author Kahle
 */
public abstract class NestedException extends Exception {

    public NestedException() {
        super();
    }

    public NestedException(String message) {
        super(message);
    }

    public NestedException(Throwable cause) {
        super(cause);
    }

    public NestedException(String message, Throwable cause) {
        super(message, cause);
    }

}
