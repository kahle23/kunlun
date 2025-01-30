/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message;

import kunlun.message.model.Message;

/**
 * The message listener.
 * @author Kahle
 */
public interface MessageListener {

    /**
     * Processing the received messages.
     * @param message The received messages
     * @return The necessary return value or null
     */
    Object onMessage(Message message);

}
