package artoria.lifecycle;

import artoria.exception.UncheckedException;

/**
 * Lifecycle exception.
 * @author Kahle
 */
public class LifecycleException extends UncheckedException {

    public LifecycleException() {

        super();
    }

    public LifecycleException(String message) {

        super(message);
    }

    public LifecycleException(Throwable cause) {

        super(cause);
    }

    public LifecycleException(String message, Throwable cause) {

        super(message, cause);
    }

}
