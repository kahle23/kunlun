package artoria.cache;

import artoria.exception.UncheckedException;

/**
 * Cache exception.
 * @author Kahle
 */
public class CacheException extends UncheckedException {

    public CacheException() {

        super();
    }

    public CacheException(String message) {

        super(message);
    }

    public CacheException(Throwable cause) {

        super(cause);
    }

    public CacheException(String message, Throwable cause) {

        super(message, cause);
    }

}
