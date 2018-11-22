package artoria.exception;

/**
 * Business exception.
 * @author Kahle
 */
public class BusinessException extends UncheckedException {
    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {

        return this.errorCode;
    }

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
        super(errorCode != null ? errorCode.getDescription() : null);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode != null ? errorCode.getDescription() : null, cause);
        this.errorCode = errorCode;
    }

}
