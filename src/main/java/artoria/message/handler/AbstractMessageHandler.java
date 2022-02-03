package artoria.message.handler;

import java.util.List;
import java.util.Map;

/**
 * The abstract message handler.
 * @author Kahle
 */
public abstract class AbstractMessageHandler implements MessageHandler {

    protected void isSupport(Class<?>[] supportClasses, Class<?> clazz) {
        if (Object.class.equals(clazz)) { return; }
        for (Class<?> supportClass : supportClasses) {
            if (supportClass.equals(clazz)) { return; }
        }
        throw new IllegalArgumentException("Parameter \"clazz\" is not supported. ");
    }

    @Override
    public <T> T send(Map<?, ?> properties, Object message, Class<T> clazz) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T batchSend(Map<?, ?> properties, List<?> messages, Class<T> clazz) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T info(Map<?, ?> properties, Object input, Class<T> clazz) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<T> search(Map<?, ?> properties, Object input, Class<T> clazz) {

        throw new UnsupportedOperationException();
    }

}
