package artoria.event;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static artoria.common.Constants.NEWLINE;
import static artoria.util.ObjectUtils.cast;

/**
 * The abstract event provider.
 * @author Kahle
 */
public abstract class AbstractEventProvider implements EventProvider {
    private static Logger log = LoggerFactory.getLogger(AbstractEventProvider.class);

    /**
     * Edit the event to add extended fields.
     * @param event The event object passed in
     */
    protected abstract void edit(Event event);

    /**
     * Push the event to specific handlers.
     * @param event The event object passed in
     */
    protected abstract void push(Event event);

    /**
     * Show the event (log printing is implemented by default).
     * @param event The event object passed in
     */
    protected void show(Event event) {
        String content = NEWLINE +
                "---- Begin Event ----" + NEWLINE +
                "Code:           " + event.getCode() + NEWLINE +
                "Time:           " + DateUtils.format(event.getTime()) + NEWLINE +
                "DistinctId:     " + event.getDistinctId() + NEWLINE +
                "AnonymousId:    " + event.getAnonymousId() + NEWLINE +
                "Provider:       " + getClass().getName() + NEWLINE +
                "---- End Event ----" + NEWLINE;
        log.info(content);
    }

    @Override
    public void add(String code, String distinctId, Map<?, ?> properties) {
        Assert.notBlank(distinctId, "Parameter \"distinctId\" must not blank. ");
        add(code, null, distinctId, null, properties);
    }

    @Override
    public void add(String code, String distinctId, String anonymousId, Map<?, ?> properties) {

        add(code, null, distinctId, anonymousId, properties);
    }

    @Override
    public void add(String code, Long time, String distinctId, String anonymousId, Map<?, ?> properties) {
        try {
            // Validate parameters, errors are not allowed to be thrown out.
            Assert.notBlank(code, "Parameter \"code\" must not blank. ");
            if (StringUtils.isBlank(distinctId) && StringUtils.isBlank(anonymousId)) {
                throw new IllegalArgumentException(
                    "Parameter \"distinctId\" and parameter \"anonymousId\" cannot both be blank. "
                );
            }
            if (properties == null) { properties = new LinkedHashMap<Object, Object>(); }
            if (time == null) { time = System.currentTimeMillis(); }
            // Build the event object.
            Event event = new Event();
            event.setCode(code);
            event.setTime(time);
            event.setDistinctId(distinctId);
            event.setAnonymousId(anonymousId);
            event.setProperties(properties);
            // Edit the event.
            edit(event);
            // Push the event.
            push(event);
        }
        catch (Exception e) {
            log.error("An error has occurred with \"" + getClass().getSimpleName() + "\". ", e);
        }
    }

    /**
     * The event object.
     */
    protected static class Event {
        private Map<Object, Object> properties;
        private String anonymousId;
        private String distinctId;
        private Long   time;
        private String code;

        public String getCode() {

            return code;
        }

        public void setCode(String code) {

            this.code = code;
        }

        public Long getTime() {

            return time;
        }

        public void setTime(Long time) {

            this.time = time;
        }

        public String getDistinctId() {

            return distinctId;
        }

        public void setDistinctId(String distinctId) {

            this.distinctId = distinctId;
        }

        public String getAnonymousId() {

            return anonymousId;
        }

        public void setAnonymousId(String anonymousId) {

            this.anonymousId = anonymousId;
        }

        public Map<Object, Object> getProperties() {

            return properties;
        }

        public void setProperties(Map<?, ?> properties) {

            this.properties = cast(properties);
        }
    }

}
