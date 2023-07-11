package artoria.lang;

/**
 * The code information.
 * @see <a href="https://en.wikipedia.org/wiki/Code">Code</a>
 * @author Kahle
 */
@Deprecated // TODO: 2022/11/19 Deletable
public interface Code<T> {

    /**
     * The value of the code.
     * @return The value
     */
    T getCode();

    /**
     * The description of the code.
     * @return The description
     */
    String getDescription();

}
