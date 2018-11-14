package artoria.cache;

import java.io.Serializable;

/**
 * Cache configuration.
 * @param <K> The type of key
 * @param <V> The type of value
 * @author Kahle
 */
public interface Configuration<K, V> extends Serializable {

    /**
     * Return the key type.
     * @return Key type
     */
    Class<K> getKeyType();

    /**
     * Return the value type.
     * @return Value type
     */
    Class<V> getValueType();

    /**
     * Get cache loader if have.
     * @return A cache loader object or null if not have
     */
    CacheLoader<K, V> getCacheLoader();

}
