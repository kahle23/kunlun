package artoria.time;

import artoria.lang.Clock;

/**
 * The clock simple implement by jdk.
 * @see java.lang.System#currentTimeMillis()
 * @see <a href="https://en.wikipedia.org/wiki/Epoch_(computing)">Epoch (computing)</a>
 * @author Kahle
 */
public class SimpleClock implements Clock {

    /**
     * The current time as UTC milliseconds from the epoch.
     * @return The time in milliseconds
     */
    @Override
    public Long getTime() {

        return System.currentTimeMillis();
    }

}
