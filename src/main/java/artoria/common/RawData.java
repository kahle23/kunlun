package artoria.common;

import java.io.Serializable;
import java.util.Map;

/**
 * Provide access to raw data.
 * @author Kahle
 */
public interface RawData extends Serializable {

    /**
     * Get data value by name.
     * @param name Data name
     * @return Data value
     */
    Object get(String name);

    /**
     * Stores the data name and value in the raw data.
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
     * Batch add raw data.
     * @param data Raw data to be added
     */
    void putAll(Map<String, Object> data);

    /**
     * Clear all raw data.
     */
    void clear();

    /**
     * Get all raw data.
     * @return All raw data
     */
    Map<String, Object> rawData();

}
