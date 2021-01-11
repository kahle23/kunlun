package artoria.time;

/**
 * Provide the highest level of abstraction for clock.
 * @author Kahle
 */
public interface Clock {

    /**
     * Get the time in milliseconds.
     * @return The time in milliseconds
     */
    long getTimeInMillis();

    /**
     * Get the time in seconds.
     * @return The time in seconds
     */
    long getTimeInSeconds();

}
