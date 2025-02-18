/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.track;

import java.util.Map;

/**
 * The track provider for event tracking.
 * @author Kahle
 */
@Deprecated // TODO: Can delete
public interface TrackProvider {

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
     * Track an event (Who did what and when).
     *
     * Normally, it is silent (not throw exception) and executes synchronously.
     * Controlling the execution of different modes through parameters is an expected implementation.
     *
     * In the parameter list, the "platform" is not necessary, because "platform" can be passed through "properties".
     * It can also be passed directly through the implementation class.
     *
     * The event category is not necessary, because it can be associated with the event code.
     *
     * The category of event code:
     *      record:*        the record type event
     *      error:*         the error type event
     *      access:*        the access type event
     *      statistic:*     the statistic type event
     *      full-track:*    the full track data type event (traceless tracking)
     *
     * @param code The event code (event identifier), indicating a certain type of event
     *             (such as user login events, can be null, get from properties)
     * @param time The time at which the event occurred
     *             (can be null, get from properties)
     * @param principal The principal information, such as the user's unique id or user object, and so on
     *                    (can be null, get from properties)
     * @param properties The other event properties (string or map or java bean)
     */
    void track(String code, Long time, Object principal, Object properties);

}
