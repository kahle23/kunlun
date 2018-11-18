package artoria.codec;

import artoria.exception.UncheckedException;

/**
 * Thrown when encode error.
 * @author Kahle
 */
public class EncodeException extends UncheckedException {

    public EncodeException() {

        super();
    }

    public EncodeException(String message) {

        super(message);
    }

    public EncodeException(Throwable cause) {

        super(cause);
    }

    public EncodeException(String message, Throwable cause) {

        super(message, cause);
    }

}
