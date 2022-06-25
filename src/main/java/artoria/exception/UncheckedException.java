package artoria.exception;

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
