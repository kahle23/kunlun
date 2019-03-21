package artoria.time;

/**
 * Provide the highest level of abstraction for Clock.
 * @author Kahle
 */
public interface Clock {

    /**
     * Get the timestamp of the current time in milliseconds.
     * @return The timestamp of the current time
     */
    long getTime();

}
