package artoria.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Provide access to extended data. Resolve the problem of passing attributes that do not exist.
 * @author Kahle
 */
public interface ExtendedData extends Serializable {

    /**
     * Get data value by name.
     * @param name Data name
     * @return Data value
     */
    Object get(String name);

    /**
     * Stores the data name and value in the extended data.
     * @param name Data name
     * @param value Data value
     * @return Old data value if have
     */
    Object put(String name, Object value);

    /**
     * Remove the data value by name.
     * @param name Data name
     * @return Removed data value if have
     */
    Object remove(String name);

    /**
     * Batch add extended data.
     * @param data Extended data to be added
     */
    void putAll(Map<String, Object> data);

    /**
     * Clear all extended data.
     */
    void clear();

    /**
     * Get all extended data.
     * @return All extended data
     */
    Map<String, Object> extendedData();

}
