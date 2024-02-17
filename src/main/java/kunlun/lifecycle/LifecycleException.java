/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.lifecycle;

import kunlun.exception.UncheckedException;

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
