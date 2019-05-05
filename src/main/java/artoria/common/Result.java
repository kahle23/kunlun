package artoria.common;

import java.io.Serializable;

/**
 * Uniform result output object.
 * @param <T> Data type
 * @author Kahle
 */
public class Result<T> implements Serializable {
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

    public Boolean getSuccess() {

        return this.success;
    }

    public void setSuccess(Boolean success) {

        this.success = success;
    }

    public String getCode() {

        return this.code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getMessage() {

        return this.message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public T getData() {

        return this.data;
    }

    public void setData(T data) {

        this.data = data;
    }

}
