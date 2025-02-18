/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.collector;

import kunlun.core.Collector;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

/**
 * The collector tools.
 * @author Kahle
 */
@Deprecated
public class CollectorUtils {
    private static final Logger log = LoggerFactory.getLogger(CollectorUtils.class);
    private static volatile CollectorProvider collectorProvider;

    public static CollectorProvider getCollectorProvider() {
        if (collectorProvider != null) { return collectorProvider; }
        synchronized (CollectorUtils.class) {
            if (collectorProvider != null) { return collectorProvider; }
            CollectorUtils.setCollectorProvider(new SimpleCollectorProvider());
            return collectorProvider;
        }
    }

    public static void setCollectorProvider(CollectorProvider collectorProvider) {
        Assert.notNull(collectorProvider, "Parameter \"collectorProvider\" must not null. ");
        log.info("Set collector provider: {}", collectorProvider.getClass().getName());
        CollectorUtils.collectorProvider = collectorProvider;
    }

    public static String getDefaultCollectorName() {

        return getCollectorProvider().getDefaultCollectorName();
    }

    public static void setDefaultCollectorName(String defaultCollectorName) {

        getCollectorProvider().setDefaultCollectorName(defaultCollectorName);
    }

    public static void registerCollector(String collectorName, Collector collector) {

        getCollectorProvider().registerCollector(collectorName, collector);
    }

    public static void deregisterCollector(String collectorName) {

        getCollectorProvider().deregisterCollector(collectorName);
    }

    public static Collector getCollector(String collectorName) {

        return getCollectorProvider().getCollector(collectorName);
    }

    public static Object collect(Object data, Object... arguments) {

        return getCollectorProvider().collect(getDefaultCollectorName(), data, arguments);
    }

    public static Object collect(String collectorName, Object data, Object... arguments) {

        return getCollectorProvider().collect(collectorName, data, arguments);
    }

}
