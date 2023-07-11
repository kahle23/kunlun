package artoria.bot;

import artoria.core.Bot;

/**
 * The message bot.
 * @deprecated 有 MessageHandler 和 MessageUtils 也够用了
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/7 Deletable
public interface MessageBot extends Bot {

    /**
     * Send a message to the specified target.
     * @param message The message to be sent
     * @return The operation result or the message reply
     */
    Object send(Object message);

}
