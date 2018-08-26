package artoria.random;

import artoria.util.Assert;

import java.util.List;
import java.util.logging.Logger;

/**
 * Random tools.
 * @author Kahle
 */
public class RandomUtils {
    private static Logger log = Logger.getLogger(RandomUtils.class.getName());
    private static Randomizer randomizer;

    static {
        RandomUtils.setRandomizer(new SimpleRandomizer());
    }

    public static Randomizer getRandomizer() {
        return randomizer;
    }

    public static void setRandomizer(Randomizer randomizer) {
        Assert.notNull(randomizer, "Parameter \"randomizer\" must not null. ");
        log.info("Set randomizer: " + randomizer.getClass().getName());
        RandomUtils.randomizer = randomizer;
    }

    public static <T> T[] confuse(T[] arr) {
        return randomizer.confuse(arr);
    }

    public static <T> List<T> confuse(List<T> list) {
        return randomizer.confuse(list);
    }

    public static <T> T nextObject(Class<T> clazz) {
        return randomizer.nextObject(clazz);
    }

    public static int nextInt() {
        return randomizer.nextInt();
    }

    public static int nextInt(int bound) {
        return randomizer.nextInt(bound);
    }

    public static long nextLong() {
        return randomizer.nextLong();
    }

    public static float nextFloat() {
        return randomizer.nextFloat();
    }

    public static double nextDouble() {
        return randomizer.nextDouble();
    }

    public static boolean nextBoolean() {
        return randomizer.nextBoolean();
    }

    public static void nextBytes(byte[] bytes) {
        randomizer.nextBytes(bytes);
    }

    public static byte[] nextBytes(int length) {
        return randomizer.nextBytes(length);
    }

    public static String nextString(int length) {
        return randomizer.nextString(length);
    }

    public static String nextString(char[] charArray, int length) {
        return randomizer.nextString(charArray, length);
    }

}
