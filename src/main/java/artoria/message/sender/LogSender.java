package artoria.message.sender;

import artoria.lang.Code;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.MessageType;

import java.util.Map;

/**
 * The sender of the message sent to the log.
 * @author Kahle
 */
public class LogSender extends ConsoleSender {
    private static Logger log = LoggerFactory.getLogger(LogSender.class);

    public LogSender() {

        this(MessageType.LOG);
    }

    public LogSender(Code<?> type) {

        super(type);
    }

    @Override
    public void send(Map<?, ?> properties, Object message) {

        log.info(convert(message, properties));
    }

}
