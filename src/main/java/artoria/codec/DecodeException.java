package artoria.codec;

import artoria.exception.UncheckedException;

/**
 * Thrown when decode error.
 * @author Kahle
 */
public class DecodeException extends UncheckedException {

    public DecodeException() {

        super();
    }

    public DecodeException(String message) {

        super(message);
    }

    public DecodeException(Throwable cause) {

        super(cause);
    }

    public DecodeException(String message, Throwable cause) {

        super(message, cause);
    }

}
