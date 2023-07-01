package artoria.core;

/**
 * Provide the highest level of abstraction for comparator.
 *
 * It provides file, picture, text similarity comparison.
 * It also provides a comparison of the differences in the data.
 *
 * For example,
 * compare the difference in user data before and after the modification.
 *
 * For example,
 * different user levels have different price discounts,
 * compare the difference in price discounts before and after the modification.
 *
 * @see java.util.Comparator
 * @author Kahle
 */
public interface Comparator {

    /**
     * Compare the left and right objects passed in.
     * @param left The left object to be compared
     * @param right The right object to be compared
     * @param arguments The arguments to be compared (maybe is configuration or feature)
     * @return The compare result
     */
    Object compare(Object left, Object right, Object... arguments);

}
