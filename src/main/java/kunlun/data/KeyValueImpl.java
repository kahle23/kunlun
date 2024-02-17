/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

import java.io.Serializable;

/**
 * An implementation class for a key-value of two elements.
 * @param <K> The key type
 * @param <V> The value type
 * @author Kahle
 */
@Deprecated
public class KeyValueImpl<K, V> implements KeyValue<K, V>, Serializable {
    private V value;
    private K key;

    public KeyValueImpl(K key, V value) {
        this.value = value;
        this.key = key;
    }

    public KeyValueImpl() {

    }

    @Override
    public K getKey() {

        return key;
    }

    public void setKey(K key) {

        this.key = key;
    }

    @Override
    public V getValue() {

        return value;
    }

    public void setValue(V value) {

        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        KeyValueImpl<?, ?> that = (KeyValueImpl<?, ?>) object;
        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }
        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KeyValueImpl{" +
                "value=" + value +
                ", key=" + key +
                '}';
    }

}
