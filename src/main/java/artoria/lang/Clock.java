package artoria.lang;

/**
 * Provide the highest level of abstraction for clock.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Clock">Clock</a>
 * @see <a href="https://en.wikipedia.org/wiki/Timestamp">Timestamp</a>
 * @author Kahle
 */
public interface Clock {

    /**
     * Get the clock time (not the current time, but the clock time).
     * @return The clock time
     */
    Object getTime();

}
