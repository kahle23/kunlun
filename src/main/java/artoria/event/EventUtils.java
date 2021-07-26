package artoria.event;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Map;

/**
 * Event tools.
 * @author Kahle
 */
public class EventUtils {
    private static Logger log = LoggerFactory.getLogger(EventUtils.class);
    private static EventProvider eventProvider;

    public static EventProvider getEventProvider() {
        if (eventProvider != null) { return eventProvider; }
        synchronized (EventUtils.class) {
            if (eventProvider != null) { return eventProvider; }
            EventUtils.setEventProvider(new SimpleEventProvider());
            return eventProvider;
        }
    }

    public static void setEventProvider(EventProvider eventProvider) {
        Assert.notNull(eventProvider, "Parameter \"eventProvider\" must not null. ");
        log.info("Set event provider: {}", eventProvider.getClass().getName());
        EventUtils.eventProvider = eventProvider;
    }

    public static void add(String code, String distinctId, Map<?, ?> properties) {

        getEventProvider().add(code, distinctId, properties);
    }

    public static void add(String code, String distinctId, String anonymousId, Map<?, ?> properties) {

        getEventProvider().add(code, distinctId, anonymousId, properties);
    }

    public static void add(String code, Long time, String distinctId, String anonymousId, Map<?, ?> properties) {

        getEventProvider().add(code, time, distinctId, anonymousId, properties);
    }

}
