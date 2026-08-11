/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.map;

import java.util.Map;

/**
 * ToMap.
 * @author Kahle
 */
public interface ToMap<K, V> {

    /**
     * The ability to convert to map.
     * @return The map that has been converted
     */
    Map<K, V> toMap();

}
