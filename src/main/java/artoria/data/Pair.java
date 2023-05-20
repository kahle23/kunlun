package artoria.data;

/**
 * A pair consisting of two elements.
 * @param <L> The left element type
 * @param <R> The right element type
 * @author Kahle
 */
@Deprecated
public interface Pair<L, R> {

    /**
     * Gets the left element from this pair.
     * @return The left element, may be null
     */
    L getLeft();

    /**
     * Gets the right element from this pair.
     * @return The right element, may be null
     */
    R getRight();

}
