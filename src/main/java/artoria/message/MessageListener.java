package artoria.message;

import artoria.core.Listener;

/**
 * The message listener.
 * @author Kahle
 */
public interface MessageListener extends Listener {

    /**
     * Processing received messages.
     * @param data The received data
     * @return The necessary return value or null
     */
    Object onMessage(Object data);

}
