/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.collector;

import kunlun.core.Collector;

import java.util.Map;

/**
 * The collector provider.
 * @author Kahle
 */
public interface CollectorProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Get the default collector name.
     * @return The default collector name
     */
    String getDefaultCollectorName();

    /**
     * Set the default collector name.
     * Depending on the implementation class, this method may throw an error
     *  (i.e. it does not allow the modification of the default collector name).
     * @param defaultCollectorName The default collector name
     */
    void setDefaultCollectorName(String defaultCollectorName);

    /**
     * Register the collector.
     * @param collectorName The collector name
     * @param collector The collector
     */
    void registerCollector(String collectorName, Collector collector);

    /**
     * Deregister the collector.
     * @param collectorName The collector name
     */
    void deregisterCollector(String collectorName);

    /**
     * Get the collector by name.
     * @param collectorName The collector name
     * @return The collector
     */
    Collector getCollector(String collectorName);

    /**
     * Collect the data.
     * @param collectorName The collector name
     * @param data The data to be collected
     * @param arguments The other auxiliary arguments during collection
     * @return The collection operation result (maybe ignorable)
     */
    Object collect(String collectorName, Object data, Object... arguments);

}
