package com.apyhs.artoria.util;

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

    public static double round(double input) {
        return NumberUtils.round(input
                , defaultScale, defaultRoundMode).doubleValue();
    }

    public static double round(String input) {
        return NumberUtils.round(input
                , defaultScale, defaultRoundMode).doubleValue();
    }

    public static double round(BigDecimal input) {
        return NumberUtils.round(input
                , defaultScale, defaultRoundMode).doubleValue();
    }

    public static BigDecimal round(double input, int newScale) {
        return NumberUtils.round(input, newScale, defaultRoundMode);
    }

    public static BigDecimal round(String input, int newScale) {
        return NumberUtils.round(input, newScale, defaultRoundMode);
    }

    public static BigDecimal round(BigDecimal input, int newScale) {
        return NumberUtils.round(input, newScale, defaultRoundMode);
    }

    public static BigDecimal round(double input, int newScale, RoundingMode roundingMode) {
        BigDecimal decimal = new BigDecimal(input);
        return decimal.setScale(newScale, roundingMode);
    }

    public static BigDecimal round(String input, int newScale, RoundingMode roundingMode) {
        BigDecimal decimal = new BigDecimal(input);
        return decimal.setScale(newScale, roundingMode);
    }

    public static BigDecimal round(BigDecimal input, int newScale, RoundingMode roundingMode) {
        return input.setScale(newScale, roundingMode);
    }

    public static String format(Object input) {
        return NumberUtils.format(defaultPattern, input);
    }

    public static String format(String pattern, Object input) {
        Format format = new DecimalFormat(pattern);
        return format.format(input);
    }

}
