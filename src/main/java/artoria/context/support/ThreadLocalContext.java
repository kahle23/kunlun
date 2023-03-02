package artoria.context.support;

import artoria.core.Context;

/**
 * Flag the current context object is based on ThreadLocal.
 *
 * There are two problems with ThreadLocal.
 *      The first is how to clean up ThreadLocal.
 *      The second is how to pass it across threads.
 *
 * @author Kahle
 */
public interface ThreadLocalContext extends Context {

    /**
     * Reset the ThreadLocal in the current context object.
     * The method should be allowed to be invoked repeatedly.
     * There should be no exceptions to this method.
     */
    void resetThreadLocals();

}
