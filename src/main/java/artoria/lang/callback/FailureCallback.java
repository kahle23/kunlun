package artoria.lang.callback;

/**
 * The failure callback.
 * @author Kahle
 */
public interface FailureCallback {

    /**
     * Called when the invoke fails.
     * @param th The exception that occurs in the invoke
     */
    void onFailure(Throwable th);

}
