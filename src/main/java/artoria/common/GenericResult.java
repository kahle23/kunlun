package artoria.common;

/**
 * Generic result.
 * @author Kahle
 */
public interface GenericResult {

    /**
     * Get success.
     * @return True or False
     */
    Boolean getSuccess();

    /**
     * Set success.
     * @param success True or False
     */
    void setSuccess(Boolean success);

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
