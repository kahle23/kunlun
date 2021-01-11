package artoria.time;

import static artoria.common.Constants.ONE_THOUSAND;

/**
 * Clock simple implement by jdk.
 * @see System#currentTimeMillis()
 * @see <a href="https://en.wikipedia.org/wiki/Epoch_(computing)">Epoch (computing)</a>
 * @author Kahle
 */
public class SimpleClock implements Clock {

    /**
     * The current time as UTC milliseconds from the epoch.
     * @return The time in milliseconds
     */
    @Override
    public long getTimeInMillis() {

        return System.currentTimeMillis();
    }

    /**
     * The current time as UTC seconds from the epoch.
     * @return The time in seconds
     */
    @Override
    public long getTimeInSeconds() {

        return getTimeInMillis() / ONE_THOUSAND;
    }

}
