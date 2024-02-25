/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

import java.util.*;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;

/**
 * The collection tools.
 * @author Kahle
 */
public class CollectionUtils {
    private static final Logger log = LoggerFactory.getLogger(CollectionUtils.class);

    public static <E> boolean isEmpty(Collection<E> collection) {

        return collection == null || collection.isEmpty();
    }

    public static <E> boolean isNotEmpty(Collection<E> collection) {

        return collection != null && !collection.isEmpty();
    }

    public static <E> boolean isEmpty(Enumeration<E> enumeration) {

        return enumeration == null || !enumeration.hasMoreElements();
    }

    public static <E> boolean isNotEmpty(Enumeration<E> enumeration) {

        return enumeration != null && enumeration.hasMoreElements();
    }

    public static <E> void reverse(List<E> list) {
        Assert.notNull(list, "Parameter \"list\" must not null. ");
        for (int start = ZERO, end = list.size() - ONE; start < end; start++, end--) {
            E temp = list.get(end);
            list.set(end, list.get(start));
            list.set(start, temp);
        }
    }

    public static <E> List<E> removeDuplicate(List<E> list) {
        Set<E> eSet = new HashSet<E>(list);
        return new ArrayList<E>(eSet);
    }

    public static <E> List<E> removeDuplicateWithOrder(List<E> list) {
        List<E> newList = new ArrayList<E>();
        Set<E> eSet = new HashSet<E>();
        for (E next : list) {
            if (eSet.add(next)) {
                newList.add(next);
            }
        }
        return newList;
    }

    public static <E> E getFirst(List<E> list) {
        if (isEmpty(list)) { return null; }
        return list.get(ZERO);
    }

    public static <E> E getLast(List<E> list) {
        if (isEmpty(list)) { return null; }
        return list.get(list.size() - ONE);
    }

    public static <T> T[] toArray(Collection<T> collection, Class<?> componentType) {
        if (collection == null) { return null; }
        T[] array = ArrayUtils.newArray(componentType, ZERO);
        return collection.toArray(array);
    }

    public static <E> void addAll(Collection<E> collection, Enumeration<E> enumeration) {
        if (collection == null || enumeration == null) { return; }
        while (enumeration.hasMoreElements()) {
            collection.add(enumeration.nextElement());
        }
    }

}
