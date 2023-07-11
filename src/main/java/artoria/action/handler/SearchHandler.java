package artoria.action.handler;

import artoria.action.ActionHandler;

import java.util.List;

/**
 * The information search handler.
 * @author Kahle
 */
@Deprecated
public interface SearchHandler extends ActionHandler {

    /**
     * Search for some information.
     * @param input The input parameters to be searched
     * @param clazz The class of the return value
     * @param <T> The generic type of the return value
     * @return The return value
     */
    <T> List<T> search(Object input, Class<T> clazz);

}
