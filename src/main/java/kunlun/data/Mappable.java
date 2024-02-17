/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

import java.util.Map;

/**
 * The ability to convert to and from map.
 * @author Kahle
 */
public interface Mappable {

    /**
     * The ability to convert back from map.
     * @param map The map to be read
     */
    void fromMap(Map<?, ?> map);

    /**
     * The ability to convert to map.
     * @return The map that has been converted
     */
    Map<Object, Object> toMap();

}
