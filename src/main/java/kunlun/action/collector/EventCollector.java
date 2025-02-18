/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.collector;

import kunlun.action.AbstractAction;
import kunlun.action.collector.model.Event;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.time.DateUtils;
import kunlun.util.Assert;

import static kunlun.common.constant.Numbers.FIVE_HUNDRED;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.NEWLINE;

/**
 * The event collector.
 * @author Kahle
 */
public class EventCollector extends AbstractAction {
    private static final Logger log = LoggerFactory.getLogger(EventCollector.class);

    /**
     * Process the event record to add extended fields.
     * @param event The event record passed in
     */
    protected void process(Event event) {

    }

    /**
     * Push the event record to specific handlers.
     * @param event The event record passed in
     */
    protected void push(Event event) {

        show(event);
    }

    /**
     * Show the event record (log printing is implemented by default).
     * @param event The event record passed in
     */
    protected void show(Event event) {
        if (event == null) { return; }
        String message = String.valueOf(event.getMessage());
        if (message.length() > FIVE_HUNDRED) {
            message = message.substring(ZERO, FIVE_HUNDRED) + " ...";
        }
        String content = NEWLINE +
                "---- Begin Event ----" + NEWLINE +
                "Name:           " + event.getName() + NEWLINE +
                "Time:           " + DateUtils.format(event.getTime()) + NEWLINE +
                "UserId:         " + event.getUserId() + NEWLINE +
                "Message:        " + message + NEWLINE +
                "Provider:       " + getClass().getName() + NEWLINE +
                "---- End Event ----" + NEWLINE;
        log.info(content);
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.isInstanceOf(Event.class, input
                , "Parameter \"data\" must instance of \"Event\". ");
        try {
            // Convert the event object.
            Event event = (Event) input;
            // Validate parameters, errors are not allowed to be thrown out.
            Assert.notBlank(event.getName(), "The event name cannot be blank. ");
            if (event.getTime() == null) {
                event.setTime(System.currentTimeMillis());
            }
            // Process the event record.
            process(event);
            // Push the event record.
            push(event);
        }
        catch (Exception e) {
            log.error("An error has occurred with \"" + getClass().getSimpleName() + "\". ", e);
        }
        // End.
        return null;
    }

}
