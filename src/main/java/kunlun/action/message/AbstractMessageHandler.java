/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.message;

import kunlun.action.AbstractAction;
import kunlun.message.MessageHandler;
import kunlun.message.model.Message;
import kunlun.message.model.Result;
import kunlun.message.model.Subscribe;
import kunlun.util.IterUtil;

import java.util.Collection;

import static java.util.Collections.singletonList;

/**
 * The abstract message handler.
 * @author Kahle
 */
public abstract class AbstractMessageHandler extends AbstractAction implements MessageHandler {
    /**
     * The standard operation name: subscribe.
     */
    protected static final String SUBSCRIBE = "subscribe";
    /**
     * The standard operation name: receive.
     */
    protected static final String RECEIVE = "receive";
    /**
     * The standard operation name: send.
     */
    protected static final String SEND = "send";

    @Override
    public Message receive(Base condition) {

        throw new UnsupportedOperationException("This method is not supported! ");
    }

    @Override
    public Result subscribe(Subscribe subscribe) {

        throw new UnsupportedOperationException("This method is not supported! ");
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        if (SEND.equals(strategy)) {
            if (input instanceof Collection) {
                //noinspection unchecked
                return send((Collection<Message>) input);
            } else {
                return send(singletonList((Message) input));
            }
        } else if (RECEIVE.equals(strategy)) {
            return receive((Base) input);
        } else if (SUBSCRIBE.equals(strategy)) {
            return subscribe((Subscribe) input);
        } else {
            if (input instanceof Collection &&
                    (IterUtil.getFirst((Collection<?>) input) instanceof Message)) {
                //noinspection unchecked
                return send((Collection<Message>) input);
            } else if (input instanceof Message) {
                return send(singletonList((Message) input));
            } else if (input instanceof Subscribe) {
                return subscribe((Subscribe) input);
            } else {
                throw new IllegalArgumentException("Unsupported input parameters type. ");
            }
        }
    }

}
