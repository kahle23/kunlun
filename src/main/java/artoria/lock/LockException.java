package artoria.lock;

import artoria.exception.UncheckedException;

/**
 * Lock exception.
 * @author Kahle
 */
public class LockException extends UncheckedException {

    public LockException() {

        super();
    }

    public LockException(String message) {

        super(message);
    }

    public LockException(Throwable cause) {

        super(cause);
    }

    public LockException(String message, Throwable cause) {

        super(message, cause);
    }

}
