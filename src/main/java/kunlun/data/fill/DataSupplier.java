package kunlun.data.fill;

import java.util.Collection;
import java.util.Map;

/**
 * 数据填充器 所需要的 数据提供者.
 *
 * @see DataFiller
 * @author Zerox
 */
public interface DataSupplier {

    /**
     * 根据传入的条件获取数据.
     * <p>
     * 外层一定是 Map，并且 Map 的 Key 一定是由“查询字段”的值组成，比如“用户ID”的值.
     * <p>
     * Map 的 Value 可以不走 Map，可以走 Java Bean（类型为 Object），但是考虑到使用者可能错误使用，
     * 直接“简单值对象”（比如 String 之类的），这样就导致了整体性异常，所以限制了 Value 也只能是 Map.
     *
     * @param coll 查询参数
     * @return 结果
     */
    Map<String, Map<String, Object>> acquire(Collection<?> coll);

}
