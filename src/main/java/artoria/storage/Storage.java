package artoria.storage;

import java.util.Collection;
import java.util.Map;

/**
 * The interface that defines common storage operations.
 * The reason some methods return object is because the implementation class might be a remote invocation class.
 * Although most of the time the return value doesn't have much use, but it can't be ignored.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/23 Deletable
public interface Storage {

    /**
     * Return the storage name.
     * @return The storage name
     */
    String getName();

    /**
     * Return the underlying native storage provider.
     * @return The native storage provider
     */
    Object getNative();

    /**
     * Get the corresponding stored contents based on the key.
     * @param key The key (possibly path) corresponding to the stored content
     * @param type The return value type
     * @param <T> The return value generic type
     * @return The storage content corresponding to the key
     */
    <T> T get(Object key, Class<T> type);

    /**
     * Get the corresponding stored contents based on the key.
     * @param key The key (possibly path) corresponding to the stored content
     * @return The storage content corresponding to the key
     */
    Object get(Object key);

    /**
     * Determines if the storage contains the contents of the specified key.
     * @param key The key whose presence in this storage is to be tested
     * @return True if this storage contains a mapping for the specified key
     */
    boolean containsKey(Object key);

    /**
     * Put the content and its corresponding key into storage.
     * @param key The key (possibly path) corresponding to the stored content
     * @param value The content to be stored
     * @return The result of execution or old stored content
     */
    Object put(Object key, Object value);

    /**
     * Batch put the contents and their corresponding keys into storage.
     * @param map The collection of mappings of keys and content to be put into storage
     * @return The result of execution
     */
    Object putAll(Map<?, ?> map);

    /**
     * Remove the content of the storage corresponding to the key.
     * @param key The key (possibly path) corresponding to the stored content
     * @return The result of execution or old stored content
     */
    Object remove(Object key);

    /**
     * Remove the contents corresponding to keys from storage in batch.
     * @param keys The key (possibly path) corresponding to the stored content
     * @return The result of execution
     */
    Object removeAll(Collection<?> keys);

    /**
     * Clear everything from the storage.
     * @return The result of execution
     */
    Object clear();

    /**
     * Find all keys matching the given pattern.
     * @param pattern The pattern at the time of the find
     * @param type The return value type
     * @param <T> The return value generic type
     * @return All keys that match a given pattern
     */
    <T> Collection<T> keys(Object pattern, Class<T> type);

}
