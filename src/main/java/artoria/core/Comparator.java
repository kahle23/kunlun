package artoria.core;

/**
 * Provide the highest level of abstraction for comparator.
 *
 * It provides file, picture, text similarity comparison.
 * It also provides a comparison of the differences in the data.
 *
 * @see java.util.Comparator
 * @author Kahle
 */
public interface Comparator {

    /**
     * Compare the left and right objects passed in.
     * @param left The left object to be compared
     * @param right The right object to be compared
     * @return The compare result
     */
    Object compare(Object left, Object right);

}
