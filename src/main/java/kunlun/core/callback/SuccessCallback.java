/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.callback;

/**
 * The success callback.
 * @param <T> The type of successful result
 * @author Kahle
 */
@Deprecated
public interface SuccessCallback<T> {

    /**
     * Called when the invoked succeeds.
     * @param result The result returned by the invoked
     */
    void onSuccess(T result);

}
