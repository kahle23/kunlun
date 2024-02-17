/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message;

import kunlun.core.Listener;

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
