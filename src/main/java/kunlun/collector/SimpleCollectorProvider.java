/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.collector;

import kunlun.collector.support.SimpleEventCollector;
import kunlun.core.Collector;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple collector provider.
 * @author Kahle
 */
public class SimpleCollectorProvider implements CollectorProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleCollectorProvider.class);
    protected final Map<String, Collector> collectors;
    protected final Map<String, Object> commonProperties;
    private String defaultCollectorName = "default";

    protected SimpleCollectorProvider(Map<String, Object> commonProperties,
                                      Map<String, Collector> collectors) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(collectors, "Parameter \"collectors\" must not null. ");
        this.commonProperties = commonProperties;
        this.collectors = collectors;
        // Register the default collector.
        registerCollector(getDefaultCollectorName(), new SimpleEventCollector());
    }

    public SimpleCollectorProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, Collector>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public String getDefaultCollectorName() {

        return defaultCollectorName;
    }

    @Override
    public void setDefaultCollectorName(String defaultCollectorName) {
        Assert.notBlank(defaultCollectorName, "Parameter \"defaultCollectorName\" must not blank. ");
        this.defaultCollectorName = defaultCollectorName;
    }

    @Override
    public void registerCollector(String collectorName, Collector collector) {
        Assert.notBlank(collectorName, "Parameter \"collectorName\" must not blank. ");
        Assert.notNull(collector, "Parameter \"collector\" must not null. ");
        String className = collector.getClass().getName();
        if (collector instanceof AbstractCollector) {
            ((AbstractCollector) collector).setCommonProperties(getCommonProperties());
        }
        collectors.put(collectorName, collector);
        log.info("Register the collector \"{}\" to \"{}\". ", className, collectorName);
    }

    @Override
    public void deregisterCollector(String collectorName) {
        Assert.notBlank(collectorName, "Parameter \"collectorName\" must not blank. ");
        Collector remove = collectors.remove(collectorName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the collector \"{}\" from \"{}\". ", className, collectorName);
        }
    }

    @Override
    public Collector getCollector(String collectorName) {
        Assert.notBlank(collectorName, "Parameter \"collectorName\" must not blank. ");
        Collector collector = collectors.get(collectorName);
        Assert.notNull(collector
                , "The corresponding collector could not be found by name. ");
        return collector;
    }

    @Override
    public Object collect(String collectorName, Object data, Object... arguments) {

        return getCollector(collectorName).collect(data, arguments);
    }

}
