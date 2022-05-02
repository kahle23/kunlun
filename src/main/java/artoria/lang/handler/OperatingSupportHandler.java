package artoria.lang.handler;

import artoria.lang.Handler;

/**
 * The operating supported handler.
 * Very similar to the policy design pattern.
 * But it is mainly to leave an extension port for callers and extenders to handle.
 * @author Kahle
 */
public interface OperatingSupportHandler extends Handler {

    /**
     * Perform an operation and return the corresponding result.
     * @param operation The name of the operation to be performed
     * @param arguments The arguments for the operation to be performed
     * @return The result of the operation
     */
    Object operate(Object operation, Object[] arguments);

}
