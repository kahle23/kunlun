package artoria.util;

import java.util.Collection;
import java.util.List;

/**
 * Collection tools.
 * @author Kahle
 */
public class CollectionUtils {

    public static <E> boolean isEmpty(Collection<E> col) {
        return col == null || col.isEmpty();
    }

    public static <E> boolean isNotEmpty(Collection<E> col) {
        return col != null && !col.isEmpty();
    }

    public static <E> void reverse(List<E> list) {
        Assert.notNull(list, "List must is not null. ");
        for (int start = 0, end = list.size() - 1; start < end; start++, end--) {
            E temp = list.get(end);
            list.set(end, list.get(start));
            list.set(start, temp);
        }
    }

}
