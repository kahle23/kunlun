package com.apyhs.artoria.exception;

import com.apyhs.artoria.util.StringUtils;

import static com.apyhs.artoria.util.StringConstant.EMPTY_STRING;
import static com.apyhs.artoria.util.StringConstant.ENDL;

/**
 * Unchecked exception.
 * @author Kahle
 */
public class UncheckedException extends RuntimeException {

    private ErrorCode errorCode;
    private String others;

    public UncheckedException() {
        super();
    }

    public UncheckedException(String message) {
        super(message);
    }

    public UncheckedException(Throwable cause) {
        super(cause);
    }

    public UncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    protected void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getOthers() {
        return others;
    }

    public UncheckedException setOthers(String others) {
        this.others = others;
        return this;
    }

    public UncheckedException setOthers(String format, Object... args) {
        this.others = String.format(format, args);
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
                    .append(errorCode.toString())
                    .append(")").append(ENDL);
        }
        if (StringUtils.isNotBlank(others)) {
            builder.append("others: ")
                    .append(others)
                    .append(ENDL);
        }
        return builder.append("stack trace: ").toString();
    }

}
