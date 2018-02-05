package com.apyhs.artoria.exception;

import com.apyhs.artoria.util.StringUtils;

import static com.apyhs.artoria.util.Const.EMPTY_STRING;
import static com.apyhs.artoria.util.Const.ENDL;

/**
 * Business exception.
 * @author Kahle
 */
public class BusinessException extends UncheckedException {

    private ErrorCode errorCode;
    private String description;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getContent());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getContent(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }

    public BusinessException setDescription(String description) {
        this.description = description;
        return this;
    }

    public BusinessException setDescription(StringBuilder builder) {
        this.description = builder.toString();
        return this;
    }

    public BusinessException setDescription(String format, Object... args) {
        this.description = String.format(format, args);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String message = this.getLocalizedMessage();
        builder.append(this.getClass().getName())
                .append(
                        StringUtils.isNotBlank(message)
                        ? ": " + message : EMPTY_STRING
                ).append(ENDL);
        if (errorCode != null) {
            builder.append(errorCode.getClass().getName())
                    .append(": ")
                    .append(errorCode.getCode())
                    .append(" (")
                    .append(errorCode.getContent())
                    .append(")").append(ENDL);
        }
        if (StringUtils.isNotBlank(description)) {
            builder.append("Description: ")
                    .append(description)
                    .append(ENDL);
        }
        return builder.append("StackTrace: ").toString();
    }

}
