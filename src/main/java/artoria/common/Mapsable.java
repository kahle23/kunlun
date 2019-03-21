package artoria.common;

import java.util.List;
import java.util.Map;

/**
 * Can convert to or convert back from map list.
 * @author Kahle
 */
public interface Mapsable {

    /**
     * Convert to map list.
     * @return Converted map list
     */
    List<Map<String, Object>> toMapList();

    /**
     * Convert back from map list.
     * @param mapList Input map list
     */
    void fromMapList(List<Map<String, Object>> mapList);

}
