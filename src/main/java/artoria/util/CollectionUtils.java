package artoria.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;

/**
 * Collection tools.
 * @author Kahle
 */
public class CollectionUtils {

    /**
     * Take java bean list first not null element.
     * @param list A java bean list
     * @param <T> Java bean type
     * @return A not null java bean
     */
    public static <T> T takeFirstNotNullElement(List<T> list) {
        if (list == null) {
            return null;
        }
        for (T bean : list) {
            if (bean != null) {
                return bean;
            }
        }
        return null;
    }

    public static <E> boolean isEmpty(Collection<E> col) {

        return col == null || col.isEmpty();
    }

    public static <E> boolean isNotEmpty(Collection<E> col) {

        return col != null && !col.isEmpty();
    }

    public static <E> void reverse(List<E> list) {
        Assert.notNull(list, "Parameter \"list\" must not null. ");
        for (int start = 0, end = list.size() - 1; start < end; start++, end--) {
            E temp = list.get(end);
            list.set(end, list.get(start));
            list.set(start, temp);
        }
    }

    public static <E> void addAll(Collection<E> collection, Enumeration<E> enumeration) {
        while (enumeration.hasMoreElements()) {
            collection.add(enumeration.nextElement());
        }
    }

    @SuppressWarnings("unchecked")
    public static <E> void sort(List<E> list, boolean isAsc, String... properties) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Object[] array = list.toArray();
        ArrayUtils.sort(array, isAsc, properties);
        ListIterator<E> li = list.listIterator();
        for (Object e : array) {
            li.next();
            li.set((E) e);
        }
    }

}
