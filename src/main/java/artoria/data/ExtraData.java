package artoria.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Provide access to extra data. Resolve the problem of passing attributes that do not exist.
 * It can pass extra data in addition to normal data, and it can also pass extended data.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/7 Deletable
public interface ExtraData extends Serializable {

    /**
     * Get data value by name.
     * @param name Data name
     * @return Data value
     */
    Object get(String name);

    /**
     * Stores the data name and value in the extra data.
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
     * Batch add extra data.
     * @param data Extra data to be added
     */
    void putAll(Map<String, Object> data);

    /**
     * Clear all extra data.
     */
    void clear();

    /**
     * Get all extra data.
     * @return All extra data
     */
    Map<String, Object> extraData();

}
