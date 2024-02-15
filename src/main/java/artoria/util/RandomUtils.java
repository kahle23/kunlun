package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import static artoria.common.constant.Numbers.ONE;
import static artoria.common.constant.Numbers.ZERO;

/**
 * The random tools.
 * @author Kahle
 */
public class RandomUtils {
    private static final char[] DEFAULT_CHAR_ARRAY = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Long TIME_INTERVAL = 6 * 60 * 60 * 1000L;
    private static volatile Long lastUpdatedTime = 0L;
    private static final Logger log = LoggerFactory.getLogger(RandomUtils.class);
    private static Random random;

    private static Random getRandom() {
        if ((System.currentTimeMillis() - lastUpdatedTime) <= TIME_INTERVAL) {
            return random;
        }
        synchronized (RandomUtils.class) {
            if ((System.currentTimeMillis() - lastUpdatedTime) <= TIME_INTERVAL) {
                return random;
            }
            random = new SecureRandom();
            lastUpdatedTime = System.currentTimeMillis();
            return random;
        }
    }

    /**
     * Confuse array elements.
     * @param arr An array will be confused
     * @param <T> Array element type
     * @return An array reference inputted
     */
    public static <T> T[] confuse(T[] arr) {
        for (int i = arr.length - ONE; i > ONE; --i) {
            int nextInt = RandomUtils.nextInt(i);
            T tmp = arr[nextInt];
            arr[nextInt] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

    /**
     * Confuse list elements.
     * @param list A list will be confused
     * @param <T> List element type
     * @return A list reference inputted
     */
    public static <T> List<T> confuse(List<T> list) {
        for (int i = list.size() - ONE; i > ONE; --i) {
            int nextInt = RandomUtils.nextInt(i);
            T tmp = list.get(nextInt);
            list.set(nextInt, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    /**
     * Random generation int.
     * @return A random int
     */
    public static int nextInt() {

        return getRandom().nextInt();
    }

    /**
     * Random generation int.
     * @param bound The int value bound
     * @return A random int
     */
    public static int nextInt(int bound) {

        return getRandom().nextInt(bound);
    }

    /**
     * Random generation long.
     * @return A random long
     */
    public static long nextLong() {

        return getRandom().nextLong();
    }

    /**
     * Random generation float.
     * @return A random float
     */
    public static float nextFloat() {

        return getRandom().nextFloat();
    }

    /**
     * Random generation double.
     * @return A random double
     */
    public static double nextDouble() {

        return getRandom().nextDouble();
    }

    /**
     * Random generation boolean.
     * @return A random boolean
     */
    public static boolean nextBoolean() {

        return getRandom().nextBoolean();
    }

    /**
     * Random generation character.
     * @return A random character
     */
    public static Character nextCharacter() {

        return nextCharacter(DEFAULT_CHAR_ARRAY);
    }

    /**
     * Random generation character.
     * @param charArray Candidate character array
     * @return A random character
     */
    public static Character nextCharacter(char[] charArray) {
        int index = RandomUtils.nextInt(charArray.length);
        return charArray[index];
    }

    /**
     * Random generation byte array.
     * @param bytes The byte array will be filled
     */
    public static void nextBytes(byte[] bytes) {

        getRandom().nextBytes(bytes);
    }

    /**
     * Random generation byte array.
     * @param length The byte array length
     * @return A random byte array
     */
    public static byte[] nextBytes(int length) {
        byte[] bytes = new byte[length];
        getRandom().nextBytes(bytes);
        return bytes;
    }

    /**
     * Random generation string.
     * @param length The string length
     * @return A random string
     */
    public static String nextString(int length) {

        return nextString(DEFAULT_CHAR_ARRAY, length);
    }

    /**
     * Random generation string.
     * @param charArray Candidate char array
     * @param length The string length
     * @return A random string
     */
    public static String nextString(char[] charArray, int length) {
        StringBuilder result = new StringBuilder();
        int len = charArray.length;
        for (int i = ZERO; i < length; i++) {
            int nextInt = getRandom().nextInt(len);
            result.append(charArray[nextInt]);
        }
        return result.toString();
    }

}
