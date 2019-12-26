package artoria.common;

/**
 * Default error code.
 * @author Kahle
 */
public enum InternalErrorCode implements ErrorCode {
    /**
     * No login.
     */
    NO_LOGIN("NO_LOGIN", "No login."),
    /**
     * Invalid token.
     */
    INVALID_TOKEN("INVALID_TOKEN", "Invalid token."),
    /**
     * Invalid user.
     */
    INVALID_USER("INVALID_USER", "Invalid user."),
    /**
     * No permission.
     */
    NO_PERMISSION("NO_PERMISSION", "No permission."),
    /**
     * Parameter is required.
     */
    PARAMETER_IS_REQUIRED("PARAMETER_IS_REQUIRED", "Parameter is required."),
    /**
     * Internal server busy.
     */
    INTERNAL_SERVER_BUSY("INTERNAL_SERVER_BUSY", "Internal server busy."),
    /**
     * Internal server error.
     */
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error."),
    ;

    private String code;
    private String description;
    private String message;

    InternalErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
        this.message = description;
    }

    InternalErrorCode(String code, String description, String message) {
        this.code = code;
        this.description = description;
        this.message = message;
    }

    @Override
    public String getCode() {

        return code;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public String getMessage() {

        return message;
    }

}
