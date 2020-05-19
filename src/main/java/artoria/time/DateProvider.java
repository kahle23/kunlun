package artoria.time;

import java.text.ParseException;
import java.util.Date;

/**
 * Date provider.
 * @author Kahle
 */
public interface DateProvider {

    /**
     * Format date object to date string.
     * @param date Date object
     * @param pattern Date format
     * @return Date string
     */
    String format(Date date, String pattern);

    /**
     * Parse date string to date object on pattern.
     * @param dateString Date string
     * @param pattern Date format
     * @return Date object
     * @throws ParseException Parse error
     */
    Date parse(String dateString, String pattern) throws ParseException;

}
