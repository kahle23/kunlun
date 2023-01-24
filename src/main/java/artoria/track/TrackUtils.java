package artoria.track;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * The track tools.
 * @author Kahle
 */
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
