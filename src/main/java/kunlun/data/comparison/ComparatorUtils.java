/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.comparison;

import kunlun.core.Comparator;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The comparator tools.
 * @author Kahle
 */
public class ComparatorUtils {
    private static final Map<String, Comparator> COMPARATORS = new ConcurrentHashMap<String, Comparator>();
    private static final Logger log = LoggerFactory.getLogger(ComparatorUtils.class);

    public static void registerComparator(String name, Comparator comparator) {
        Assert.notNull(comparator, "Parameter \"comparator\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = comparator.getClass().getName();
        log.info("Register \"{}\" to \"{}\". ", className, name);
        COMPARATORS.put(name, comparator);
    }

    public static Comparator deregisterComparator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Comparator remove = COMPARATORS.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister \"{}\" to \"{}\". ", className, name);
        }
        return remove;
    }

    public static Comparator getComparator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return COMPARATORS.get(name);
    }

    public static <T> T compare(String name, Object left, Object right, Object... arguments) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Comparator comparator = COMPARATORS.get(name);
        Assert.state(comparator != null,
                "The comparator named \"" + name + "\" could not be found. ");
        return ObjectUtils.cast(comparator.compare(left, right, arguments));
    }

}
