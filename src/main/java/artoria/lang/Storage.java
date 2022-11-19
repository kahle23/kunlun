package artoria.lang;

/**
 * Provide the highest level of abstraction for storage.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Computer_data_storage">Computer data storage</a>
 * @see <a href="https://en.wikipedia.org/wiki/Data_storage">Data storage</a>
 * @author Kahle
 */
public interface Storage {

    /**
     * Determine whether the data exists based on the resource information (key).
     * @param key The resource information (key)
     * @return The exist or not exist
     */
    boolean exist(Object key);

    /**
     * Obtain the data based on the resource information (key).
     * @param key The resource information (key)
     * @return The data
     */
    Object get(Object key);

    /**
     * Put the data.
     * @param data The data
     * @return The old data or null
     */
    Object put(Object data);

    /**
     * Delete the data based on the resource information (key).
     * @param key The resource information (key)
     * @return The deleted data or null
     */
    Object delete(Object key);

    /**
     * Condition query the resource information list.
     * @param conditions The query conditions
     * @return The query result or null
     */
    Object list(Object conditions);

}
