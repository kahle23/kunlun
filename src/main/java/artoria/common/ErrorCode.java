package artoria.common;

/**
 * Error code.
 * @author Kahle
 */
public interface ErrorCode {

    /**
     * The string code of the error.
     * @return The string code
     */
    String getCode();

    /**
     * The description of the error.
     * @return The description
     */
    String getDescription();

    /**
     * The message to display when this error occur.
     * @return The message
     */
    String getMessage();

}
