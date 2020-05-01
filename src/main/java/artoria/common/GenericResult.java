package artoria.common;

/**
 * Generic result.
 * @author Kahle
 */
public interface GenericResult {

    /**
     * Get status code.
     * @return Status code
     */
    String getCode();

    /**
     * Set status code.
     * @param code Status code
     */
    void setCode(String code);

    /**
     * Get message content.
     * @return Message content
     */
    String getMessage();

    /**
     * Set message content.
     * @param message Message content
     */
    void setMessage(String message);

}
