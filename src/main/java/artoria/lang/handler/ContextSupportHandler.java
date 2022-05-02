package artoria.lang.handler;

import artoria.lang.Context;
import artoria.lang.Handler;

/**
 * The context supported handler.
 * @author Kahle
 */
public interface ContextSupportHandler extends Handler {

    /**
     * Build the context object from the arguments array.
     * Only the implementer knows what the arguments mean.
     * @param arguments The specific arguments array
     * @return The handler context object
     */
    HandlerContext buildContext(Object... arguments);

    /**
     * The handler context.
     * @author Kahle
     */
    interface HandlerContext extends Context {

        /**
         * Get an array of arguments for constructing the context.
         * @return The input arguments array
         */
        Object[] getArguments();

    }

}
