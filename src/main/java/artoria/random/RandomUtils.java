package artoria.random;

import artoria.util.Assert;

import java.util.List;
import java.util.logging.Logger;

/**
 * Random tools.
 * @author Kahle
 */
public class RandomUtils {
    private static final Randomizer DEFAULT_RANDOMIZER = new SimpleRandomizer();
    private static Logger log = Logger.getLogger(RandomUtils.class.getName());
    private static Randomizer randomizer;

    public static Randomizer getRandomizer() {
        return randomizer != null
                ? randomizer : DEFAULT_RANDOMIZER;
    }

    public static void setRandomizer(Randomizer randomizer) {
        Assert.notNull(randomizer, "Parameter \"randomizer\" must not null. ");
        log.info("Set randomizer: " + randomizer.getClass().getName());
        RandomUtils.randomizer = randomizer;
    }

    public static <T> T[] confuse(T[] arr) {

        return getRandomizer().confuse(arr);
    }

    public static <T> List<T> confuse(List<T> list) {

        return getRandomizer().confuse(list);
    }

    public static <T> T nextObject(Class<T> clazz) {

        return getRandomizer().nextObject(clazz);
    }

    public static int nextInt() {

        return getRandomizer().nextInt();
    }

    public static int nextInt(int bound) {

        return getRandomizer().nextInt(bound);
    }

    public static long nextLong() {

        return getRandomizer().nextLong();
    }

    public static float nextFloat() {

        return getRandomizer().nextFloat();
    }

    public static double nextDouble() {

        return getRandomizer().nextDouble();
    }

    public static boolean nextBoolean() {

        return getRandomizer().nextBoolean();
    }

    public static void nextBytes(byte[] bytes) {

        getRandomizer().nextBytes(bytes);
    }

    public static byte[] nextBytes(int length) {

        return getRandomizer().nextBytes(length);
    }

    public static String nextString(int length) {

        return getRandomizer().nextString(length);
    }

    public static String nextString(char[] charArray, int length) {

        return getRandomizer().nextString(charArray, length);
    }

}
