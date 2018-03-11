package com.github.kahlkn.artoria.jdbc;

import com.github.kahlkn.artoria.exception.UncheckedException;

/**
 * Jdbc exception.
 * @author Kahle
 */
public class JdbcException extends UncheckedException {

    public JdbcException() {
        super();
    }

    public JdbcException(String message) {
        super(message);
    }

    public JdbcException(Throwable cause) {
        super(cause);
    }

    public JdbcException(String message, Throwable cause) {
        super(message, cause);
    }

}
