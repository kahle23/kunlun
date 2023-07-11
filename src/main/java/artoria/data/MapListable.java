package artoria.data;

import java.util.List;
import java.util.Map;

/**
 * Can convert to or convert back from map list.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/7 Deletable
public interface MapListable extends Listable {

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
