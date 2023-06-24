package artoria.core;

/**
 * Serialize an object into data or deserialize data into an object.
 * @author Kahle
 */
public interface Serializer {

    /**
     * Encode object to data.
     *
     * @param object The object to serialize
     * @return The serialized data
     */
    Object serialize(Object object);

    /**
     * Decode object from data.
     *
     * @param data The data to be deserialized
     * @return The deserialized object
     */
    Object deserialize(Object data);

}
