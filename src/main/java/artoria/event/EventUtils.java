package artoria.event;

import artoria.track.TrackProvider;
import artoria.track.TrackUtils;

/**
 * The event tools.
 * @see artoria.track.TrackUtils
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/7 Deletable
public class EventUtils {

    public static TrackProvider getEventProvider() {

        return TrackUtils.getTrackProvider();
    }

    public static void setEventProvider(TrackProvider trackProvider) {

        TrackUtils.setTrackProvider(trackProvider);
    }

    public static void track(Object properties) {

        TrackUtils.track(properties);
    }

    public static void track(String code, Object properties) {

        TrackUtils.track(code, properties);
    }

    public static void track(String code, Object principal, Object properties) {

        TrackUtils.track(code, principal, properties);
    }

    public static void track(String code, Long time, Object principal, Object properties) {

        TrackUtils.track(code, time, principal, properties);
    }

}
