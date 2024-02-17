/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

/**
 * The data dictionary object.
 * What is a data dictionary? A list that explains and describes specific information.
 * @author Kahle
 */
public interface Dict {

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

    /**
     * Get the description of the dictionary item.
     * @return The description of the dictionary item
     */
    String getDescription();

    /**
     * Get the sort of the dictionary item.
     * @return The sort of the dictionary item
     */
    Integer getSort();

    /**
     * Get the extra data of the dictionary item.
     * The extra data or incidental data (maybe it's extended data).
     * @return The extra data (most case is Map)
     */
    Object getExtraData();

}
