package artoria.action.handler;

/**
 * The information query handler.
 * @author Kahle
 */
public interface InfoHandler extends ActionHandler {

    /**
     * Query some information.
     * @param input The input parameters to be queried
     * @param clazz The class of the return value
     * @param <T> The generic type of the return value
     * @return The return value
     */
    <T> T info(Object input, Class<T> clazz);

}
