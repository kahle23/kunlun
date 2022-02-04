package artoria.action.handler;

/**
 * The logical execute handler.
 * @author Kahle
 */
public interface ExecuteHandler extends ActionHandler {

    /**
     * Execute a specific logic.
     * @param input The input parameters to be executed
     * @param clazz The class of the return value
     * @param <T> The generic type of the return value
     * @return The return value
     */
    <T> T execute(Object input, Class<T> clazz);

}
