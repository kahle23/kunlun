package artoria.random;

import java.util.List;

/**
 * Random generator.
 * @author Kahle
 */
public interface Randomizer {

    /**
     * Confuse array elements.
     * @param arr A array will be confuse
     * @param <T> Array element type
     * @return A array reference inputted
     */
    <T> T[] confuse(T[] arr);

    /**
     * Confuse list elements.
     * @param list A list will be confuse
     * @param <T> List element type
     * @return A list reference inputted
     */
    <T> List<T> confuse(List<T> list);

    /**
     * Random generation int.
     * @return A random int
     */
    int nextInt();

    /**
     * Random generation int.
     * @param bound The int value bound
     * @return A random int
     */
    int nextInt(int bound);

    /**
     * Random generation long.
     * @return A random long
     */
    long nextLong();

    /**
     * Random generation float.
     * @return A random float
     */
    float nextFloat();

    /**
     * Random generation double.
     * @return A random double
     */
    double nextDouble();

    /**
     * Random generation boolean.
     * @return A random boolean
     */
    boolean nextBoolean();

    /**
     * Random generation byte array.
     * @param bytes The byte array will be filled
     */
    void nextBytes(byte[] bytes);

    /**
     * Random generation byte array.
     * @param length The byte array length
     * @return A random byte array
     */
    byte[] nextBytes(int length);

    /**
     * Random generation string.
     * @param length The string length
     * @return A random string
     */
    String nextString(int length);

    /**
     * Random generation string.
     * @param charArray Candidate char array
     * @param length The string length
     * @return A random string
     */
    String nextString(char[] charArray, int length);

    /**
     * Randomly generates objects of the specified type.
     * @param clazz The object class
     * @param <T> The object type
     * @return A random object
     */
    <T> T nextObject(Class<T> clazz);

}
