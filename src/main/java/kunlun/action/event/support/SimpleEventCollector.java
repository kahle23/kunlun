/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.event.support;

import kunlun.action.AbstractAction;
import kunlun.action.event.Event;
import kunlun.action.event.EventCollector;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.time.DateUtil;
import kunlun.util.Assert;

import static kunlun.common.constant.Numbers.FIVE_HUNDRED;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.NEWLINE;

/**
 * The simple event collector.
 * @author Kahle
 */
public class SimpleEventCollector extends AbstractAction implements EventCollector {
    private static final Logger log = LoggerFactory.getLogger(SimpleEventCollector.class);

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
                "Time:           " + DateUtil.format(event.getTime()) + NEWLINE +
                "UserId:         " + event.getUserId() + NEWLINE +
                "Message:        " + message + NEWLINE +
                "Provider:       " + getClass().getName() + NEWLINE +
                "---- End Event ----" + NEWLINE;
        log.info(content);
    }

    @Override
    public void process(Event event) {

    }

    @Override
    public void push(Event event) {

        show(event);
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        Assert.isInstanceOf(Event.class, Assert.notNull(input));
        Assert.notBlank(((Event) input).getName());
        try {
            // Convert the event object.
            Event event = (Event) input;
            if (event.getTime() == null) {
                event.setTime(System.currentTimeMillis());
            }
            // Process the event record.
            process(event);
            // Push the event record.
            push(event);
        } catch (Exception e) {
            log.error("An error has occurred with \"" + getClass().getSimpleName() + "\". ", e);
        }
        return null;
    }

}
