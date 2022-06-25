package artoria.util;

import java.util.Map;

/**
 * The map tools.
 * @author Kahle
 */
public class MapUtils {

    public static <K, V> boolean isEmpty(Map<K, V> map) {

        return map == null || map.isEmpty();
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {

        return map != null && !map.isEmpty();
    }

}
