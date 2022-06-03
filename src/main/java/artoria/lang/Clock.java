package artoria.lang;

/**
 * Provide the highest level of abstraction for clock.
 * @see <a href="https://en.wikipedia.org/wiki/Clock">Clock</a>
 * @see <a href="https://en.wikipedia.org/wiki/Timestamp">Timestamp</a>
 * @author Kahle
 */
public interface Clock {

    /**
     * Get the clock raw time (return the millisecond timestamp whenever possible).
     * @return The clock raw time
     */
    Object getRawTime();

    /**
     * Get the time in milliseconds.
     * @return The time in milliseconds
     */
    long getTimeInMillis();

}
