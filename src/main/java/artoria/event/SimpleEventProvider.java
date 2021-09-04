package artoria.event;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.time.DateUtils;
import artoria.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.*;
import static java.util.Collections.emptyList;

/**
 * The simple event provider.
 * @author Kahle
 */
public class SimpleEventProvider extends AbstractEventProvider {
    private static Logger log = LoggerFactory.getLogger(SimpleEventProvider.class);
    private List<String> showPropertyNames;

    public SimpleEventProvider() {

        this(Collections.<String>emptyList());
    }

    public SimpleEventProvider(List<String> showKeys) {

        setShowPropertyNames(showKeys);
    }

    public List<String> getShowPropertyNames() {

        return showPropertyNames;
    }

    public void setShowPropertyNames(List<String> showPropertyNames) {
        if (showPropertyNames == null) { showPropertyNames = emptyList(); }
        this.showPropertyNames = showPropertyNames;
    }

    @Override
    protected void process(Event event) {

    }

    @Override
    protected void push(Event event) {

        show(event);
    }

    @Override
    protected void show(Event event) {
        // Build the string of the event properties.
        Map<Object, Object> properties = event.getProperties();
        StringBuilder builder = new StringBuilder();
        for (String propertyName : showPropertyNames) {
            if (StringUtils.isBlank(propertyName)) { continue; }
            Object value = properties.get(propertyName);
            if (value == null) { continue; }
            propertyName = StringUtils.capitalize(propertyName);
            builder.append(propertyName).append(COLON);
            int length = TWENTY - propertyName.length() - ONE;
            if (length <= ZERO) { length = ONE; }
            for (int i = ZERO; i < length; i++) {
                builder.append(BLANK_SPACE);
            }
            builder.append(value).append(NEWLINE);
        }
        // Build the string to print and print it.
        String content = NEWLINE +
                "---- Begin Event ----" + NEWLINE +
                "Code:               " + event.getCode() + NEWLINE +
                "Time:               " + DateUtils.format(event.getTime()) + NEWLINE +
                "DistinctId:         " + event.getDistinctId() + NEWLINE +
                "AnonymousId:        " + event.getAnonymousId() + NEWLINE +
                "Provider:           " + getClass().getName() + NEWLINE +
                builder +
                "---- End Event ----" + NEWLINE;
        log.info(content);
    }

}
