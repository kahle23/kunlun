/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

/**
 * A key value pair consisting of two elements.
 * @param <K> The key type
 * @param <V> The value type
 * @author Kahle
 */
@Deprecated
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
