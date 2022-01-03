package artoria.lang.callback;

/**
 * The success callback.
 * @param <T> The type of successful result
 * @author Kahle
 */
public interface SuccessCallback<T> {

    /**
     * Called when the invoke succeeds.
     * @param result The result returned by the invoke
     */
    void onSuccess(T result);

}
