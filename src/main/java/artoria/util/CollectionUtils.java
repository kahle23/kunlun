package artoria.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

/**
 * Collection tools.
 * @author Kahle
 */
public class CollectionUtils {

    /**
     * Get java bean list first not null element.
     * @param list A java bean list
     * @param <T> Java bean type
     * @return A not null java bean
     */
    public static <T> T firstNotNullElement(List<T> list) {
        if (list == null) { return null; }
        for (T bean : list) {
            if (bean != null) { return bean; }
        }
        return null;
    }

    public static <E> boolean isEmpty(Collection<E> collection) {

        return collection == null || collection.isEmpty();
    }

    public static <E> boolean isNotEmpty(Collection<E> collection) {

        return collection != null && !collection.isEmpty();
    }

    public static <E> void reverse(List<E> list) {
        Assert.notNull(list, "Parameter \"list\" must not null. ");
        for (int start = 0, end = list.size() - 1; start < end; start++, end--) {
            E temp = list.get(end);
            list.set(end, list.get(start));
            list.set(start, temp);
        }
    }

    public static <E> boolean isEmpty(Enumeration<E> enumeration) {

        return enumeration == null || !enumeration.hasMoreElements();
    }

    public static <E> boolean isNotEmpty(Enumeration<E> enumeration) {

        return enumeration != null && enumeration.hasMoreElements();
    }

    public static <E> void addAll(Collection<E> collection, Enumeration<E> enumeration) {
        if (collection == null || enumeration == null) { return; }
        while (enumeration.hasMoreElements()) {
            collection.add(enumeration.nextElement());
        }
    }

}
