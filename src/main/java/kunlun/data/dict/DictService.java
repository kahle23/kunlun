/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import kunlun.common.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The data dictionary service.
 * @author Kahle
 */
public interface DictService {

    // region ======== the default namespace ========
    /**
     * Get the default namespace.
     * @return The default namespace
     */
    String getDefaultNamespace();

    /**
     * Set the default namespace.
     * @param defaultNamespace The default namespace
     */
    void setDefaultNamespace(String defaultNamespace);
    // endregion


    // region ======== dictionary sync ========
    /**
     * Synchronize the list of dictionary items based on the dictionary group code.
     * All dictionary items under this group code must be provided (include: add, update, delete).
     * (item's code cannot be null)
     * @param data The dictionary data to be synchronized
     */
    void syncByGroup(Collection<Dict> data);

    /**
     * Synchronize the list of dictionary items based on their group code and item code (or id).
     * (item's scope and group and code cannot be null)
     * @param data The dictionary data to be synchronized
     */
    void syncByCode(Collection<Dict> data);
    // endregion


    // region ======== get single dictionary ========
    /**
     * Get the dict object by the dictionary item name.
     * @param namespace The dictionary namespace
     * @param groupCode The dictionary group code
     * @param name The dictionary item name
     * @return The dictionary item or null
     */
    Dict getByName(String namespace, String groupCode, String name);

    /**
     * Get the dict object by the dictionary item code.
     * @param namespace The dictionary namespace
     * @param groupCode The dictionary group code
     * @param code The dictionary item code
     * @return The dictionary item or null
     */
    Dict getByCode(String namespace, String groupCode, String code);

    /**
     * Get the dict object by the dictionary item value.
     * @param namespace The dictionary namespace
     * @param groupCode The dictionary group code
     * @param value The dictionary item value
     * @return The dictionary item or null
     */
    Dict getByValue(String namespace, String groupCode, String value);

    /**
     * Condition query a dictionary item (multiple items will error).
     * @param condition The dictionary query condition
     * @return The dictionary item or null
     */
    Dict getByCondition(DictQuery condition);
    // endregion


    // region ======== get multiple dictionaries ========
    /**
     * Query the dictionary items list by item group code.
     * @param namespace The dictionary namespace
     * @param groupCode The dictionary group code
     * @return The list of dictionary items
     */
    List<Dict> listByGroup(String namespace, String groupCode);

    /**
     * Query the dictionary items map by item group code.
     * @param namespace The dictionary namespace
     * @param groupCode The dictionary group code
     * @return The map of dictionary items
     */
    Map<String, Dict> mapByGroup(String namespace, String groupCode);

    /**
     * Condition query the dictionary items list or page.
     * @param paged Determine whether pagination is required
     * @param condition The dictionary query condition
     * @return The list or page of dictionary items
     */
    Page<Dict> listByCondition(boolean paged, DictQuery condition);
    // endregion

}
