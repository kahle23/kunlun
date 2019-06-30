package artoria.common;

import java.util.Map;

/**
 * Can convert to or convert back from map.
 * @author Kahle
 */
public interface Mappable {

    /**
     * Convert to map.
     * @return Converted map
     */
    Map<String, Object> toMap();

    /**
     * Convert back from map.
     * @param map Input map
     */
    void fromMap(Map<String, Object> map);

}
