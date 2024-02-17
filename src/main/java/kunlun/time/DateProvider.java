/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.time;

import java.util.Date;

/**
 * The date provider.
 * @author Kahle
 */
public interface DateProvider {

    /**
     * Get the default pattern.
     * @return The default pattern
     */
    String getDefaultPattern();

    /**
     * Register the date pattern.
     * @param pattern The pattern describing the date and time format
     */
    void register(String pattern);

    /**
     * Deregister the date pattern.
     * @param pattern The pattern describing the date and time format
     */
    void deregister(String pattern);

    /**
     * Formats a date into a date/time string.
     * @param date The time value to be formatted into a time string
     * @return The formatted time string
     */
    String format(Date date);

    /**
     * Formats a date into a date/time string.
     * @param date The time value to be formatted into a time string
     * @param pattern The pattern describing the date and time format
     * @return The formatted time string
     */
    String format(Date date, String pattern);

    /**
     * Parses text from the beginning of the given string to produce a date.
     * @param dateString A string whose beginning should be parsed
     * @return A date parsed from the string
     */
    Date parse(String dateString);

    /**
     * Parses text from the beginning of the given string to produce a date.
     * @param dateString A string whose beginning should be parsed
     * @param pattern The pattern describing the date and time format
     * @return A date parsed from the string
     */
    Date parse(String dateString, String pattern);

}
