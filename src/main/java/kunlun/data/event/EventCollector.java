/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.event;

import kunlun.data.Event;

/**
 * The event collector (used to process and push event data).
 * @author Kahle
 */
public interface EventCollector {

    /**
     * Process the event record to add extended fields.
     * @param event The event record passed in
     */
    void process(Event event);

    /**
     * Push the event record to specific handlers.
     * @param event The event record passed in
     */
    void push(Event event);

}
