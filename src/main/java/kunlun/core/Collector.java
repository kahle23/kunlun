/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for collector.
 * Usage scenarios:
 *      garbage collector, data collector, performance monitoring collector, log collector, etc.
 *
 * @author Kahle
 */
public interface Collector {

    /**
     * Collect the data.
     * @param data The data to be collected
     * @param arguments The other auxiliary arguments during collection
     * @return The collection operation result (maybe ignorable)
     */
    Object collect(Object data, Object... arguments);

}
