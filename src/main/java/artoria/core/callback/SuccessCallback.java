package artoria.core.callback;

/**
 * The success callback.
 * @param <T> The type of successful result
 * @author Kahle
 */
public interface SuccessCallback<T> {

    /**
     * Called when the invoked succeeds.
     * @param result The result returned by the invoked
     */
    void onSuccess(T result);

}
