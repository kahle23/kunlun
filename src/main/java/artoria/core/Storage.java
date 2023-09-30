package artoria.core;

/**
 * Provide the highest level of abstraction for storage.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Computer_data_storage">Computer data storage</a>
 * @see <a href="https://en.wikipedia.org/wiki/Data_storage">Data storage</a>
 * @author Kahle
 */
public interface Storage {

    /**
     * Perform storage related operations.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param arguments The arguments required for the operation
     * @return The result of the operation
     */
    Object execute(Object[] arguments);

}
