package artoria.io.storage;

import artoria.lang.Storage;

import java.util.Collection;

/**
 * The normal storage.
 * @author Kahle
 */
public interface NormalStorage extends Storage {

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
     * Delete data from storage in batches.
     * @param keys The keys to be deleted
     * @return The result or null
     */
    Object deleteAll(Collection<?> keys);

}
