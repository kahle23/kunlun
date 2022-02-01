package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.handler.ConsoleMessageHandler;
import artoria.message.handler.LogMessageHandler;
import artoria.message.handler.MessageHandler;

import java.util.Map;

/**
 * The simple message provider.
 * @author Kahle
 */
public class SimpleMessageProvider extends AbstractMessageProvider {
    private static Logger log = LoggerFactory.getLogger(SimpleMessageProvider.class);

    protected SimpleMessageProvider(Map<String, Object> commonProperties,
                                 Map<String, MessageHandler> messageHandlers) {
        super(commonProperties, messageHandlers);
    }

    public SimpleMessageProvider() {
        registerHandler(String.class, "console", new ConsoleMessageHandler());
        registerHandler(String.class, "log", new LogMessageHandler());
    }

}
