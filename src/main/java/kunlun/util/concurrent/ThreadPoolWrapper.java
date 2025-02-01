/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util.concurrent;

import java.util.concurrent.Executor;

/**
 * The thread pool wrapper. Provide support
 *      for cross-thread-pool propagation in thread local.
 * @author Kahle
 */
public interface ThreadPoolWrapper {

    /**
     * Wrap the thread pool (most likely to generate a proxy object).
     * @param executor The thread pool to be wrapped
     * @return The wrapped thread pool
     */
    Executor wrap(Executor executor);

}
