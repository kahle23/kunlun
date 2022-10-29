package artoria.data;

/**
 * A key value pair consisting of two elements.
 * @param <K> The key type
 * @param <V> The value type
 * @author Kahle
 */
public interface KeyValue<K, V> {

    /**
     * Gets the key from this key-value.
     * @return The key from this key-value
     */
    K getKey();

    /**
     * Gets the value from this key-value.
     * @return The value from this key-value
     */
    V getValue();

}
