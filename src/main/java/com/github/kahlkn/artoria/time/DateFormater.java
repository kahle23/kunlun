package com.github.kahlkn.artoria.time;

import java.util.Date;

/**
 * Date formater.
 * @author Kahle
 */
public interface DateFormater {

    /**
     * Format date object to date string.
     * @param date Date object
     * @param pattern Date format
     * @return Date string
     */
    String format(Date date, String pattern);

}
