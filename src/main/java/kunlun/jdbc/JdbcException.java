package kunlun.jdbc;

import kunlun.exception.UncheckedException;

/**
 * Jdbc exception.
 * @author Kahle
 */
@Deprecated
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
