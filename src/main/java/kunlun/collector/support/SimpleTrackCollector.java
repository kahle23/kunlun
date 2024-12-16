/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.collector.support;

import kunlun.collector.AbstractCollector;
import kunlun.collector.support.model.TrackRecord;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.time.DateUtils;
import kunlun.util.Assert;

import static kunlun.common.constant.Numbers.FIVE_HUNDRED;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.NEWLINE;

/**
 * The simple track collector.
 * @author Kahle
 */
public class SimpleTrackCollector extends AbstractCollector {
    private static final Logger log = LoggerFactory.getLogger(SimpleTrackCollector.class);

    /**
     * Process the track record to add extended fields.
     * @param track The track record passed in
     */
    protected void process(TrackRecord track) {

    }

    /**
     * Push the track record to specific handlers.
     * @param track The track record passed in
     */
    protected void push(TrackRecord track) {

        show(track);
    }

    /**
     * Show the track record (log printing is implemented by default).
     * @param track The track record passed in
     */
    protected void show(TrackRecord track) {
        if (track == null) { return; }
        String message = String.valueOf(track.getMessage());
        if (message.length() > FIVE_HUNDRED) {
            message = message.substring(ZERO, FIVE_HUNDRED) + " ...";
        }
        String content = NEWLINE +
                "---- Begin Event ----" + NEWLINE +
                "Code:           " + track.getCode() + NEWLINE +
                "Time:           " + DateUtils.format(track.getTime()) + NEWLINE +
                "UserId:         " + track.getUserId() + NEWLINE +
                "Message:        " + message + NEWLINE +
                "Provider:       " + getClass().getName() + NEWLINE +
                "---- End Event ----" + NEWLINE;
        log.info(content);
    }

    @Override
    public Object collect(Object data, Object... arguments) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Assert.isInstanceOf(TrackRecord.class, data
                , "Parameter \"data\" must instance of \"TrackRecord\". ");
        try {
            // Convert the track object.
            TrackRecord track = (TrackRecord) data;
            // Validate parameters, errors are not allowed to be thrown out.
            Assert.notBlank(track.getCode(), "The track code cannot be blank. ");
            if (track.getTime() == null) {
                track.setTime(System.currentTimeMillis());
            }
            // Process the track record.
            process(track);
            // Push the track record.
            push(track);
        }
        catch (Exception e) {
            log.error("An error has occurred with \"" + getClass().getSimpleName() + "\". ", e);
        }
        // End.
        return null;
    }

}
