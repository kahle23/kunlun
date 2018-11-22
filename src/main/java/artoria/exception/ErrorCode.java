package artoria.exception;

/**
 * Error code.
 * @author Kahle
 */
public interface ErrorCode {

    /**
     * The string code for error code object.
     * @return The string code
     */
    String getCode();

    /**
     * The description for error code.
     * @return The description
     */
    String getDescription();

}
