/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import java.util.Iterator;
import java.util.List;

import static kunlun.common.constant.Numbers.ZERO;

/**
 * The iterable and iterator tools.
 * @author Kahle
 */
public class IteratorUtils {

    public static boolean isEmpty(Iterable<?> iterable) {

        return iterable == null || isEmpty(iterable.iterator());
    }

    public static boolean isEmpty(Iterator<?> iterator) {

        return iterator == null || !iterator.hasNext();
    }

    public static boolean isNotEmpty(Iterable<?> iterable) {

        return iterable != null && isNotEmpty(iterable.iterator());
    }

    public static boolean isNotEmpty(Iterator<?> iterator) {

        return iterator != null && iterator.hasNext();
    }

    public static <T> T getFirst(Iterable<T> iterable) {
        if (iterable == null) { return null; }
        if (iterable instanceof List) {
            List<T> list = (List<T>) iterable;
            return CollectionUtils.isEmpty(list) ? null: list.get(ZERO);
        }
        return getFirst(iterable.iterator());
    }

    public static <T> T getFirst(Iterator<T> iterator) {
        if (iterator == null) { return null; }
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

}
