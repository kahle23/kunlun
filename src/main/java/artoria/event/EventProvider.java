package artoria.event;

import java.util.Map;

/**
 * The event provider.
 * @author Kahle
 */
public interface EventProvider {



    /**
     * Track an event (Who did what and when).
     * In the parameter list, the "platform" is not necessary, because "platform" can be passed through "properties".
     * It can also be passed directly through the implementation class.
     * The event category is not necessary, because it can be associated with the event code.
     * @param code The event code (event identifier), indicating a certain type of event (such as user login events)
     * @param time The time at which the event occurred
     * @param distinctId The distinct id, such as the user's unique id, and so on
     * @param anonymousId The anonymous id, such as visitor id or token id, etc
     * @param properties The other event properties
     */
    void track(String code, Long time, String distinctId, String anonymousId, Map<?, ?> properties);

}
