package artoria.time;

import java.text.ParseException;
import java.util.Date;

/**
 * Date parser.
 * @author Kahle
 */
public interface DateParser {

    /**
     * Parse date string to date object on pattern.
     * @param dateString Date string
     * @param pattern Date format
     * @return Date object
     * @throws ParseException Parse error
     */
    Date parse(String dateString, String pattern) throws ParseException;

}
