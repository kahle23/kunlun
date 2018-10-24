package artoria.jdbc;

import artoria.exception.UncheckedException;

/**
 * Jdbc exception.
 * @author Kahle
 */
public class DatabaseException extends UncheckedException {

    public DatabaseException() {

        super();
    }

    public DatabaseException(String message) {

        super(message);
    }

    public DatabaseException(Throwable cause) {

        super(cause);
    }

    public DatabaseException(String message, Throwable cause) {

        super(message, cause);
    }

}
