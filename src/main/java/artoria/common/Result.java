package artoria.common;

import java.io.Serializable;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Uniform result output object.
 * @param <T> Data type
 * @author Kahle
 */
public class Result<T> implements GenericResult, Serializable {
    private Boolean success;
    private String code;
    private String message;
    private T data;

    public Result() {

        this(TRUE, null, null, null);
    }

    public Result(T data) {

        this(TRUE, null, null, data);
    }

    public Result(String code, String message) {

        this(FALSE, code, message, null);
    }

    public Result(Boolean success, String code, String message) {

        this(success, code, message, null);
    }

    public Result(Boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Boolean getSuccess() {

        return success;
    }

    public void setSuccess(Boolean success) {

        this.success = success;
    }

    @Override
    public String getCode() {

        return code;
    }

    @Override
    public void setCode(String code) {

        this.code = code;
    }

    @Override
    public String getMessage() {

        return message;
    }

    @Override
    public void setMessage(String message) {

        this.message = message;
    }

    public T getData() {

        return data;
    }

    public void setData(T data) {

        this.data = data;
    }

}
