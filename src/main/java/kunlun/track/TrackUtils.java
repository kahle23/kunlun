/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.track;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

/**
 * The track tools.
 * @author Kahle
 */
@Deprecated // TODO: Can delete
public class TrackUtils {
    private static final Logger log = LoggerFactory.getLogger(TrackUtils.class);
    private static volatile TrackProvider trackProvider;

    public static TrackProvider getTrackProvider() {
        if (trackProvider != null) { return trackProvider; }
        synchronized (TrackUtils.class) {
            if (trackProvider != null) { return trackProvider; }
            TrackUtils.setTrackProvider(new SimpleTrackProvider());
            return trackProvider;
        }
    }

    public static void setTrackProvider(TrackProvider trackProvider) {
        Assert.notNull(trackProvider, "Parameter \"trackProvider\" must not null. ");
        log.info("Set track provider: {}", trackProvider.getClass().getName());
        TrackUtils.trackProvider = trackProvider;
    }

    public static void track(Object properties) {

        getTrackProvider().track(null, null, null, properties);
    }

    public static void track(String code, Object properties) {

        getTrackProvider().track(code, null, null, properties);
    }

    public static void track(String code, Object principal, Object properties) {

        getTrackProvider().track(code, null, principal, properties);
    }

    public static void track(String code, Long time, Object principal, Object properties) {

        getTrackProvider().track(code, time, principal, properties);
    }

}
