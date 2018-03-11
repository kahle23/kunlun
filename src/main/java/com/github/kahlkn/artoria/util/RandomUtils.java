package com.github.kahlkn.artoria.util;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.github.kahlkn.artoria.util.Const.MINUS;

/**
 * Random tools.
 * @author Kahle
 */
public class RandomUtils {
    private static final char[] DEFAULT_CHAR_ARRAY = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Random RANDOM = new SecureRandom();

    public static <T> T[] confuse(T[] arr) {
        for (int i = arr.length - 1; i > 1; --i) {
            int nextInt = RANDOM.nextInt(i);
            T tmp = arr[nextInt];
            arr[nextInt] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

    public static <T> List<T> confuse(List<T> list) {
        for (int i = list.size() - 1; i > 1; --i) {
            int nextInt = RANDOM.nextInt(i);
            T tmp = list.get(nextInt);
            list.set(nextInt, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static String nextUUID() {
        return UUID.randomUUID().toString();
    }

    public static String nextUUID(String separator) {
        String uuid = UUID.randomUUID().toString();
        if (separator != null && !MINUS.equals(separator)) {
            return StringUtils.replace(uuid, MINUS, separator);
        }
        else {
            return uuid;
        }
    }

    public static byte[] nextBytes(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    public static String nextString(int length) {
        return RandomUtils.nextString(DEFAULT_CHAR_ARRAY, length);
    }

    public static String nextString(char[] charArray, int length) {
        StringBuilder result = new StringBuilder();
        int len = charArray.length;
        for (int i = 0; i < length; i++) {
            int nextInt = RANDOM.nextInt(len);
            result.append(charArray[nextInt]);
        }
        return result.toString();
    }

    public static BigDecimal nextBigDecimal(int length) {
        StringBuilder result = new StringBuilder();
        result.append(RANDOM.nextInt(9) + 1);
        int len = length - 1;
        for (int i = 0; i < len; i++) {
            result.append(RANDOM.nextInt(10));
        }
        return new BigDecimal(result.toString());
    }

}
