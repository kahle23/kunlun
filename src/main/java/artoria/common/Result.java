package artoria.common;

import java.io.Serializable;

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

        this.success = true;
    }

    public Result(T data) {
        this();
        this.data = data;
    }

    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public Boolean getSuccess() {

        return success;
    }

    @Override
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
