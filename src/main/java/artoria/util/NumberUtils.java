package artoria.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;

/**
 * Number tools.
 * @author Kahle
 */
public class NumberUtils {
    private static RoundingMode defaultRoundMode = RoundingMode.HALF_UP;
    private static String defaultPattern = ".00";
    private static int defaultScale = 2;

    public static RoundingMode getDefaultRoundMode() {

        return defaultRoundMode;
    }

    public static void setDefaultRoundMode(RoundingMode roundMode) {

        defaultRoundMode = roundMode;
    }

    public static String getDefaultPattern() {

        return defaultPattern;
    }

    public static void setDefaultPattern(String pattern) {

        defaultPattern = pattern;
    }

    public static int getDefaultScale() {

        return defaultScale;
    }

    public static void setDefaultScale(int scale) {

        defaultScale = scale;
    }

    public static BigDecimal round(Object input) {

        return NumberUtils.round(input, defaultScale, defaultRoundMode);
    }

    public static String format(Object input) {

        return NumberUtils.format(input, defaultPattern);
    }

    public static BigDecimal round(Object input, int newScale) {

        return NumberUtils.round(input, newScale, defaultRoundMode);
    }

    public static String format(Object input, String pattern) {
        Format format = new DecimalFormat(pattern);
        return format.format(input);
    }

    public static BigDecimal round(Object input, int newScale, RoundingMode roundingMode) {
        BigDecimal decimal;
        if (input instanceof BigDecimal) {
            decimal = (BigDecimal) input;
        }
        else if (input instanceof String) {
            decimal = new BigDecimal((String) input);
        }
        else if (input instanceof Number) {
            double value = ((Number) input).doubleValue();
            decimal = BigDecimal.valueOf(value);
        }
        else {
            throw new IllegalArgumentException("Parameter \"input\" must String or Number. ");
        }
        return decimal.setScale(newScale, roundingMode);
    }

}
