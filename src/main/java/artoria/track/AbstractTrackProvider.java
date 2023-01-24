package artoria.track;

import artoria.core.Builder;
import artoria.data.bean.BeanUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.NEWLINE;
import static artoria.util.ObjectUtils.cast;

/**
 * The abstract track provider.
 * @author Kahle
 */
public abstract class AbstractTrackProvider implements TrackProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractTrackProvider.class);
    protected final Map<String, Object> commonProperties;

    protected AbstractTrackProvider(Map<String, Object> commonProperties) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        this.commonProperties = commonProperties;
    }

    /**
     * Create an event object with the input parameters.
     * @param code The event code
     * @param time The time at which the event occurred
     * @param principal The principal information
     * @param properties The other event properties
     * @return The event object
     */
    protected Event createEvent(String code, Long time, Object principal, Object properties) {
        // Build a map by properties.
        // The properties to override the common properties.
        Map<Object, Object> map = new LinkedHashMap<Object, Object>(getCommonProperties());
        if (properties instanceof Builder) {
            properties = ((Builder) properties).build();
        }
        if (properties == null) {
            // Do nothing.
        }
        else if (ClassUtils.isSimpleValueType(properties.getClass())) {
            String message = String.valueOf(properties);
            map.put("message", message);
        }
        else if (properties instanceof Map) {
            map.putAll((Map<?, ?>) properties);
        }
        else if (properties instanceof Iterable) {
            map.put("data", properties);
        }
        else {
            map.putAll(BeanUtils.beanToMap(properties));
        }
        // The parameters extraction.
        // Because the event code is required,
        // it must be extracted when the event object is created
        if (StringUtils.isBlank(code)) {
            code = (String) map.get("code");
        }
        if (StringUtils.isBlank(code)) {
            code = (String) map.get("eventCode");
        }
        // Build the event object.
        Event event = new Event();
        event.setCode(code);
        event.setTime(time);
        event.setPrincipal(principal);
        event.setProperties(map);
        return event;
    }

    /**
     * Process the event to add extended fields.
     * @param event The event object passed in
     */
    protected abstract void process(Event event);

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
                "Principal:      " + event.getPrincipal() + NEWLINE +
                "Provider:       " + getClass().getName() + NEWLINE +
                "---- End Event ----" + NEWLINE;
        log.info(content);
    }

    public AbstractTrackProvider() {

        this(new ConcurrentHashMap<String, Object>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, String.valueOf(entry.getValue()));
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
    public void track(String code, Long time, Object principal, Object properties) {
        try {
            // Build the event object.
            Event event = createEvent(code, time, principal, properties);
            // Validate parameters, errors are not allowed to be thrown out.
            Assert.notNull(event, "The built event object cannot be null. ");
            Assert.notBlank(event.getCode(), "The event code cannot be blank. ");
            if (event.getProperties() == null) {
                event.setProperties(new LinkedHashMap<Object, Object>());
            }
            if (event.getTime() == null) {
                event.setTime(System.currentTimeMillis());
            }
            // Process the event.
            process(event);
            // Push the event.
            push(event);
        }
        catch (Exception e) {
            log.error("An error has occurred with \"" + getClass().getSimpleName() + "\". ", e);
        }
    }

    /**
     * The internal simple event object.
     * @author Kahle
     */
    protected static class Event {
        private Map<Object, Object> properties;
        private Object principal;
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

        public Object getPrincipal() {

            return principal;
        }

        public void setPrincipal(Object principal) {

            this.principal = principal;
        }

        public Map<Object, Object> getProperties() {

            return properties;
        }

        public void setProperties(Map<?, ?> properties) {

            this.properties = cast(properties);
        }
    }

}
