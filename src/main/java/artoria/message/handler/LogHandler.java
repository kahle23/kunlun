package artoria.message.handler;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * The handler of the message sent to the log.
 * @author Kahle
 */
public class LogHandler extends ConsoleHandler {
    private static Logger log = LoggerFactory.getLogger(LogHandler.class);

    @Override
    public <T> T send(Map<?, ?> properties, Object message, Class<T> clazz) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        isSupport(new Class[]{Boolean.class}, clazz);
        log.info(convert(message, properties));
        return ObjectUtils.cast(Boolean.TRUE, clazz);
    }

    @Override
    public <T> T batchSend(Map<?, ?> properties, List<?> messages, Class<T> clazz) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not empty. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        isSupport(new Class[]{Boolean.class}, clazz);
        for (Object message : messages) {
            log.info(convert(message, properties));
        }
        return ObjectUtils.cast(Boolean.TRUE, clazz);
    }

}
