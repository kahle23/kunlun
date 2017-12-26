package com.apyhs.artoria.exception;

/**
 * Unsupported exception.
 * @author Kahle
 */
public class UnsupportedException extends RuntimeException {

    public UnsupportedException() {
        super();
    }

    public UnsupportedException(String message) {
        super(message);
    }

    public UnsupportedException(Throwable cause) {
        super(cause);
    }

    public UnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }

}
