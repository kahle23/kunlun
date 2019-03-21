package artoria.time;

import java.util.Date;

/**
 * Date formatter.
 * @author Kahle
 */
public interface DateFormatter {

    /**
     * Format date object to date string.
     * @param date Date object
     * @param pattern Date format
     * @return Date string
     */
    String format(Date date, String pattern);

}
