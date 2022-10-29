package artoria.data;

/**
 * The error code information.
 * @see <a href="https://en.wikipedia.org/wiki/Error_code">Error code</a>
 * @author Kahle
 */
public interface ErrorCode extends CodeDefinition {

    /**
     * Get the code of the error code.
     * @return The code of the error code
     */
    @Override
    String getCode();

}
