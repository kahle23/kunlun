package artoria.common;

import artoria.data.ErrorCode;

/**
 * The built-in common error codes.
 *
 * Example: H419S129
 * H4: The platform identification or system identification
 * 19: The business identification
 * S : The type of error
 * 129: The error increment code
 *
 * Type of error
 * P: Parameter related errors
 * B: Business related errors
 * S: System related errors
 * N: Network related errors
 *
 * @author Kahle
 */
public enum Errors implements ErrorCode {
    /**
     * Record not exist.
     */
    RECORD_NOT_EXIST(      "A011B101", "Record not exist."),

    /**
     * Parameter is required.
     */
    PARAMETER_IS_REQUIRED( "A011P101", "Parameter is required."),
    /**
     * Parameter format error.
     */
    PARAMETER_FORMAT_ERROR("A011P102", "Parameter format error."),

    /**
     * Internal server error.
     */
    INTERNAL_SERVER_ERROR( "A011S101", "Internal server error."),
    /**
     * Internal server busy.
     */
    INTERNAL_SERVER_BUSY(  "A011S102", "Internal server busy."),

    /**
     * No login.
     */
    NO_LOGIN(              "A012S101", "No login."),
    /**
     * Invalid token
     */
    INVALID_TOKEN(         "A012S102", "Invalid token."),
    /**
     * Invalid user.
     */
    INVALID_USER(          "A012S103", "Invalid user."),
    /**
     * No permission.
     */
    NO_PERMISSION(         "A012S104", "No permission."),
    ;

    private final String description;
    private final String code;

    Errors(String code, String description) {
        this.description = description;
        this.code = code;
    }

    @Override
    public String getCode() {

        return code;
    }

    @Override
    public String getDescription() {

        return description;
    }

}
