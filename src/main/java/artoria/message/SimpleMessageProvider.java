package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.sender.ConsoleSender;
import artoria.message.sender.LogSender;
import artoria.message.sender.MessageSender;

import java.util.Map;

/**
 * The simple message provider.
 * @author Kahle
 */
public class SimpleMessageProvider extends AbstractMessageProvider {
    private static Logger log = LoggerFactory.getLogger(SimpleMessageProvider.class);

    protected SimpleMessageProvider(Map<String, Object> commonProperties,
                                 Map<String, MessageSender> messageSenders) {
        super(commonProperties, messageSenders);
    }

    public SimpleMessageProvider() {
        registerSender(String.class, "console", new ConsoleSender());
        registerSender(String.class, "log", new LogSender());
    }

}
