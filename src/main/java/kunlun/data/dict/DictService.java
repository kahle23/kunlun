/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import java.io.Serializable;
import java.util.Collection;

/**
 * The data dictionary service.
 * @author Kahle
 */
public interface DictService {

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
     * @return The dictionary item or null
     */
    Dict getByName(String group, String name);

    /**
     * Get the dict object by the dictionary item code.
     * @param group The dictionary item group information
     * @param code The dictionary item code
     * @return The dictionary item or null
     */
    Dict getByCode(String group, String code);

    /**
     * Get the dict object by the dictionary item value.
     * @param group The dictionary item group information
     * @param value The dictionary item value
     * @return The dictionary item or null
     */
    Dict getByValue(String group, String value);

    /**
     * Condition query a dictionary item (multiple items will error).
     * @param condition The dictionary query condition (multiple types may be supported)
     * @return The dictionary item or null
     */
    Dict getByCondition(DictQuery condition);

    /**
     * Query the dictionary items list by item group information.
     * @param group The dictionary item group information
     * @return The list of dictionary items
     */
    Collection<Dict> listByGroup(String group);

    /**
     * Condition query the dictionary items list.
     * @param condition The dictionary query condition (multiple types may be supported)
     * @return The list of dictionary items
     */
    Collection<Dict> listByCondition(DictQuery condition);


    /**
     * The data dictionary query condition.
     * @author Kahle
     */
    interface DictQuery extends Serializable {

        /**
         * Get the group information of the dictionary item.
         * @return The group information of the dictionary item
         */
        String getGroup();

        /**
         * Get the name of the dictionary item.
         * @return The name of the dictionary item
         */
        String getName();

        /**
         * Get the code of the dictionary item.
         * @return The code of the dictionary item
         */
        String getCode();

        /**
         * Get the value of the dictionary item.
         * @return The value of the dictionary item
         */
        String getValue();

    }

}
