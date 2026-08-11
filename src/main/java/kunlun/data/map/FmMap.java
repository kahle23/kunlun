/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.map;

import java.util.Map;

/**
 * FromMap.
 * @author Kahle
 */
public interface FmMap {

    /**
     * The ability to convert back from map.
     * @param map The map to be read
     */
    void fromMap(Map<?, ?> map);

}
