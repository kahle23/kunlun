/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.common.constant.Nil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.EMPTY_STRING;
import static kunlun.util.Assert.*;

/**
 * The number tools.
 * @author Zerox
 */
public class NumberUtil {
    private static volatile FormatProcessor formatProcessor;
    private static volatile RoundProcessor roundProcessor;


    // region ======== 格式化处理器和保留小数处理器管理 ========

    public static FormatProcessor getFormatProcessor() {
        if (formatProcessor != null) { return formatProcessor; }
        synchronized (NumberUtil.class) {
            if (formatProcessor != null) { return formatProcessor; }
            NumberUtil.setFormatProcessor(new FormatProcessorImpl());
            return formatProcessor;
        }
    }

    public static void setFormatProcessor(FormatProcessor formatProcessor) {

        NumberUtil.formatProcessor = notNull(formatProcessor);
    }

    public static RoundProcessor getRoundProcessor() {
        if (roundProcessor != null) { return roundProcessor; }
        synchronized (NumberUtil.class) {
            if (roundProcessor != null) { return roundProcessor; }
            NumberUtil.setRoundProcessor(new RoundProcessorImpl());
            return roundProcessor;
        }
    }

    public static void setRoundProcessor(RoundProcessor roundProcessor) {

        NumberUtil.roundProcessor = notNull(roundProcessor);
    }
    // endregion


    // region ======== 将 BigDecimal 转换成字符串 ========

    public static String toStr(BigDecimal decimal) {

        return toStr(decimal, Boolean.TRUE);
    }

    public static String toStr(BigDecimal decimal, boolean isStripTrailingZeros) {
        notNull(decimal, "Parameter \"decimal\" must not blank. ");
        if (isStripTrailingZeros) { decimal = decimal.stripTrailingZeros(); }
        return decimal.toPlainString();
    }
    // endregion


    // region ======== 转换成 BigDecimal 相关方法 ========
    /**
     * 将 Number 转换为 BigDecimal
     * @param number 要转换的 Number
     * @return 转换后的 BigDecimal
     */
    public static BigDecimal toBigDecimal(Number number) {
        if (number == null) { return BigDecimal.ZERO; }
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else if (number instanceof Long) {
            return new BigDecimal((Long) number);
        } else if (number instanceof Integer) {
            return new BigDecimal((Integer) number);
        } else if (number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        } else {
            // 针对 float, double 等，先转换成 string
            return new BigDecimal(String.valueOf(number));
        }
    }

    /**
     * 将字符串的数字转换为 BigDecimal
     * @param number 要转换的字符串数字
     * @return 转换后的 BigDecimal
     */
    public static BigDecimal toBigDecimal(String number) {
        notBlank(number, "Parameter \"number\" must not blank. ");
        return new BigDecimal(number);
    }

    /**
     * 将 Object 转换为 BigDecimal
     * @param number 要转换的 Object （只支持字符串和数字）
     * @return 转换后的 BigDecimal
     */
    public static BigDecimal toBigDecimal(Object number) {
        if (number instanceof Number) {
            return toBigDecimal((Number) number);
        } else if (number instanceof String) {
            return toBigDecimal((String) number) ;
        } else {
            throw new IllegalArgumentException("Parameter \"number\" must only String or Number. ");
        }
    }
    // endregion


    // region ======== 数字格式化相关方法 ========

    public static String format(Object number) {

        return getFormatProcessor().format(number, Nil.STR, Nil.<RoundingMode>g(), Nil.FLT6);
    }

    public static String format(Object number, String pattern) {

        return getFormatProcessor().format(number, pattern, Nil.<RoundingMode>g(), Nil.FLT6);
    }

    public static String format(Object number, String pattern, RoundingMode roundingMode) {

        return getFormatProcessor().format(number, pattern, roundingMode, Nil.FLT6);
    }

    public static String format(Object number, String pattern, RoundingMode roundingMode, BigDecimal threshold) {

        return getFormatProcessor().format(number, pattern, roundingMode, threshold);
    }
    // endregion


    // region ======== 保留小数相关方法 ========

    public static BigDecimal round(Object number) {

        return getRoundProcessor().round(number, Nil.INT, Nil.<RoundingMode>g(), Nil.FLT6);
    }

    public static BigDecimal round(Object number, int newScale) {

        return getRoundProcessor().round(number, newScale, Nil.<RoundingMode>g(), Nil.FLT6);
    }

    public static BigDecimal round(Object number, int newScale, RoundingMode roundingMode) {

        return getRoundProcessor().round(number, newScale, roundingMode, Nil.FLT6);
    }

    public static BigDecimal round(Object number, int newScale, RoundingMode roundingMode, BigDecimal threshold) {

        return getRoundProcessor().round(number, newScale, roundingMode, threshold);
    }
    // endregion


    // region ======== 接口声明 ========
    /**
     * 数字格式化处理器
     * @author Zerox
     */
    public interface FormatProcessor {

        String getDefaultPattern();

        void setDefaultPattern(String pattern);

        RoundingMode getDefaultRoundingMode();

        void setDefaultRoundingMode(RoundingMode roundingMode);

        BigDecimal getDefaultThreshold();

        void setDefaultThreshold(BigDecimal defaultThreshold);

        String format(Object number, String pattern, RoundingMode roundingMode, BigDecimal threshold);

    }

    /**
     * 数字保留小数位处理器
     * @author Zerox
     */
    public interface RoundProcessor {

        int getDefaultScale();

        void setDefaultScale(int defaultScale);

        RoundingMode getDefaultRoundingMode();

        void setDefaultRoundingMode(RoundingMode defaultRoundingMode);

        BigDecimal getDefaultThreshold();

        void setDefaultThreshold(BigDecimal defaultThreshold);

        BigDecimal round(Object number, Integer newScale, RoundingMode roundingMode, BigDecimal threshold);

    }
    // endregion


    // region ======== 接口的实现类 ========
    /**
     * 简单的数字格式化处理器
     * @author Zerox
     */
    public static class FormatProcessorImpl implements FormatProcessor {
        private RoundingMode defaultRoundingMode = RoundingMode.HALF_UP;
        private BigDecimal defaultThreshold;
        private String defaultPattern = "0.00";

        @Override
        public String getDefaultPattern() {

            return defaultPattern;
        }

        @Override
        public void setDefaultPattern(String pattern) {

            this.defaultPattern = notBlank(pattern);
        }

        @Override
        public RoundingMode getDefaultRoundingMode() {

            return defaultRoundingMode;
        }

        @Override
        public void setDefaultRoundingMode(RoundingMode roundingMode) {

            this.defaultRoundingMode = notNull(roundingMode);
        }

        @Override
        public BigDecimal getDefaultThreshold() {

            return defaultThreshold;
        }

        @Override
        public void setDefaultThreshold(BigDecimal defaultThreshold) {

            this.defaultThreshold = defaultThreshold;
        }

        @Override
        public String format(Object number, String pattern, RoundingMode roundingMode, BigDecimal threshold) {
            // 判空
            if (ObjUtil.isEmpty(number)) { return EMPTY_STRING; }
            // 默认值处理
            if (roundingMode == null) { roundingMode = getDefaultRoundingMode(); }
            if (StrUtil.isBlank(pattern)) { pattern = getDefaultPattern(); }
            if (threshold == null) { threshold = getDefaultThreshold(); }
            // 数字类型转换
            BigDecimal decimal = NumberUtil.toBigDecimal(number);
            // 如果数字比阈值小且大于0，则原样输出
            if (threshold != null) {
                BigDecimal absDecimal = decimal.abs();
                if (absDecimal.compareTo(threshold) < ZERO &&
                        absDecimal.compareTo(BigDecimal.ZERO) > ZERO) {
                    return NumberUtil.toStr(decimal);
                }
            }
            // 构建格式化器
            DecimalFormat format = new DecimalFormat(pattern);
            format.setRoundingMode(roundingMode);
            // 格式化
            return format.format(number);
        }
    }

    /**
     * 简单的数字保留小数位处理器
     * @author Zerox
     */
    public static class RoundProcessorImpl implements RoundProcessor {
        private RoundingMode defaultRoundingMode = RoundingMode.HALF_UP;
        private BigDecimal defaultThreshold;
        private int defaultScale = 2;

        @Override
        public int getDefaultScale() {

            return defaultScale;
        }

        @Override
        public void setDefaultScale(int defaultScale) {
            isTrue(defaultScale >= ZERO, "Parameter \"scale\" must greater than or equal to 0. ");
            this.defaultScale = defaultScale;
        }

        @Override
        public RoundingMode getDefaultRoundingMode() {

            return defaultRoundingMode;
        }

        @Override
        public void setDefaultRoundingMode(RoundingMode roundingMode) {

            this.defaultRoundingMode = notNull(roundingMode);
        }

        @Override
        public BigDecimal getDefaultThreshold() {

            return defaultThreshold;
        }

        @Override
        public void setDefaultThreshold(BigDecimal defaultThreshold) {

            this.defaultThreshold = defaultThreshold;
        }

        @Override
        public BigDecimal round(Object number, Integer newScale, RoundingMode roundingMode, BigDecimal threshold) {
            // 判空
            if (ObjUtil.isEmpty(number)) { return null; }
            // 默认值处理
            if (roundingMode == null) { roundingMode = getDefaultRoundingMode(); }
            if (threshold == null) { threshold = getDefaultThreshold(); }
            if (newScale == null) { newScale = getDefaultScale(); }
            // 数字类型转换
            BigDecimal decimal = NumberUtil.toBigDecimal(number);
            // 如果数字比阈值小且大于0，则原样输出
            if (threshold != null) {
                BigDecimal absDecimal = decimal.abs();
                if (absDecimal.compareTo(threshold) < ZERO &&
                        absDecimal.compareTo(BigDecimal.ZERO) > ZERO) {
                    return decimal.stripTrailingZeros();
                }
            }
            // 保留小数位
            return decimal.setScale(newScale, roundingMode);
        }
    }
    // endregion

}
