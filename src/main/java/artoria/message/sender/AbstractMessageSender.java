package artoria.message.sender;

import artoria.lang.Code;
import artoria.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The abstract message sender.
 * @author Kahle
 */
public abstract class AbstractMessageSender implements MessageSender {
    private final Code<?> type;

    public AbstractMessageSender(Code<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        this.type = type;
    }

    @Override
    public Code<?> getType() {

        return type;
    }

    @Override
    public Object batchSend(Map<?, ?> properties, List<?> messages) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not empty. ");
        List<Object> result = new ArrayList<Object>();
        for (Object message : messages) {
            Object send = send(properties, message);
            result.add(send);
        }
        return result;
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
