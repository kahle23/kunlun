/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

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
