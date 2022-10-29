package artoria.data;

import java.io.Serializable;

/**
 * An implementation class for a key-value pair of two elements.
 * @param <K> The key type
 * @param <V> The value type
 * @author Kahle
 */
public class KeyValuePair<K, V> implements KeyValue<K, V>, Pair<K, V>, Serializable {
    private V value;
    private K key;

    public KeyValuePair() {
    }

    public KeyValuePair(K key, V value) {
        this.value = value;
        this.key = key;
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
    public K getLeft() {

        return getKey();
    }

    @Override
    public V getRight() {

        return getValue();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        KeyValuePair<?, ?> that = (KeyValuePair<?, ?>) object;
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
        return "KeyValuePair{" +
                "value=" + value +
                ", key=" + key +
                '}';
    }

}
