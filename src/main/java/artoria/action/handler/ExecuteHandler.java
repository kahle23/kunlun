package artoria.action.handler;

import artoria.action.ActionHandler;

import java.lang.reflect.Type;

/**
 * The logical execute handler.
 * @author Kahle
 */
@Deprecated
public interface ExecuteHandler extends ActionHandler {

    /**
     * Execute a specific logic.
     * @param input The input parameters to be executed
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The return value
     */
    <T> T execute(Object input, Type type);

}
