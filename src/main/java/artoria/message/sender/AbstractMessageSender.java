package artoria.message.sender;

import artoria.lang.Code;
import artoria.util.Assert;

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
    public void batchSend(Map<?, ?> properties, List<?> messages) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not empty. ");
        for (Object message : messages) {
            if (message == null) { continue; }
            send(properties, message);
        }
    }

}
