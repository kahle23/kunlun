package artoria.data.dict;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * The data dictionary provider.
 * @author Kahle
 */
public interface DictProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Synchronize dictionary data according to different strategies.
     *
     * Mode 1:
     * Synchronize the list of dictionary items based on their group and code.
     * (item's group and code cannot be null)
     *
     * Mode 2:
     * Synchronize the list of dictionary items based on the dictionary item group information.
     * All dictionary items under this group must be provided (include: add, update, delete).
     * (item's code cannot be null)
     *
     * @param strategy The data synchronization strategy
     * @param data The dictionary data to be synchronized
     */
    void sync(Object strategy, Object data);

    /**
     * Get the dict object by the dictionary item name.
     * @param group The dictionary item group information
     * @param name The dictionary item name
     * @return The dictionary object
     */
    Dict getByName(String group, String name);

    /**
     * Get the dict object by the dictionary item code.
     * @param group The dictionary item group information
     * @param code The dictionary item code
     * @return The dictionary object
     */
    Dict getByCode(String group, String code);

    /**
     * Get the dict object by the dictionary item value.
     * @param group The dictionary item group information
     * @param value The dictionary item value
     * @return The dictionary object
     */
    Dict getByValue(String group, String value);

    /**
     * Condition query a dictionary item (multiple items will error).
     * @param dictQuery The dictionary query condition (multiple types may be supported)
     * @return The dictionary item or null
     */
    Dict findOne(Object dictQuery);

    /**
     * Condition query the dictionary items list.
     * @param dictQuery The dictionary query condition (multiple types may be supported)
     * @param dataType The type of dictionary data
     * @param <T> The generic type of dictionary data
     * @return The list of dictionary items
     */
    <T> List<T> findMultiple(Object dictQuery, Type dataType);

}
