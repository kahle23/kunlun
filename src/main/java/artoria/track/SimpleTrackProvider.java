package artoria.track;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.time.DateUtils;
import artoria.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static artoria.common.constant.Numbers.*;
import static artoria.common.constant.Symbols.*;
import static java.util.Collections.emptyList;

/**
 * The simple track provider.
 * @author Kahle
 */
public class SimpleTrackProvider extends AbstractTrackProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleTrackProvider.class);
    private Collection<String> showPropertyNames;

    protected SimpleTrackProvider(Map<String, Object> commonProperties) {

        super(commonProperties);
    }

    public SimpleTrackProvider() {

        this(Arrays.asList("serverId", "serverAppId", "processTime", "message", "summary"));
    }

    public SimpleTrackProvider(Collection<String> showKeys) {

        setShowPropertyNames(showKeys);
    }

    public Collection<String> getShowPropertyNames() {

        return showPropertyNames;
    }

    public void setShowPropertyNames(Collection<String> showPropertyNames) {
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
            String valueOf = String.valueOf(value);
            if (valueOf.length() > TWO_HUNDRED) {
                valueOf = valueOf.substring(ZERO, TWO_HUNDRED) + " ...";
            }
            builder.append(valueOf).append(NEWLINE);
        }
        // Build the string to print and print it.
        String content = NEWLINE +
                "---- Begin Event ----" + NEWLINE +
                "Code:               " + event.getCode() + NEWLINE +
                "Time:               " + DateUtils.format(event.getTime()) + NEWLINE +
                "Principal:          " + event.getPrincipal() + NEWLINE +
                "Provider:           " + getClass().getName() + NEWLINE +
                builder +
                "---- End Event ----" + NEWLINE;
        log.info(content);
    }

}
