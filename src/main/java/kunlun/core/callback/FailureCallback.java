/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.callback;

/**
 * The failure callback.
 * @author Kahle
 */
@Deprecated
public interface FailureCallback {

    /**
     * Called when the invoked fails.
     * @param th The exception that occurs in the invoked
     */
    void onFailure(Throwable th);

}
