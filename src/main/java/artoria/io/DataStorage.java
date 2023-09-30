package artoria.io;

import artoria.core.Storage;

import java.util.Collection;

/**
 * The data storage.
 * @author Kahle
 */
public interface DataStorage extends Storage {

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
     * Put the key and value into the storage.
     * @param key The key to be stored
     * @param value The value to be stored
     * @return The old data or null
     */
    Object put(Object key, Object value);

    /**
     * Put data into storage in batches.
     * @param data The data to be put in
     * @return The result or null
     */
    Object putAll(Collection<?> data);

    /**
     * Delete the data based on the resource information (key).
     * @param key The resource information (key)
     * @return The deleted data or null
     */
    Object delete(Object key);

    /**
     * Delete data from storage in batches.
     * @param keys The keys to be deleted
     * @return The result or null
     */
    Object deleteAll(Collection<?> keys);

    /**
     * Condition query the resource information list.
     * @param conditions The query conditions
     * @return The query result or null
     */
    Object list(Object conditions);

}
