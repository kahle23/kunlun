package artoria.core.callback;

/**
 * The failure callback.
 * @author Kahle
 */
public interface FailureCallback {

    /**
     * Called when the invoked fails.
     * @param th The exception that occurs in the invoked
     */
    void onFailure(Throwable th);

}
