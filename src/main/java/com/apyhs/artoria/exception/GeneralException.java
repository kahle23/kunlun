package com.apyhs.artoria.exception;

import com.apyhs.artoria.util.MapUtils;
import com.apyhs.artoria.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.apyhs.artoria.util.StringConstant.EMPTY_STRING;
import static com.apyhs.artoria.util.StringConstant.ENDL;

/**
 * General exception.
 * @author Kahle
 */
public class GeneralException extends NestedRuntimeException {

    private ExceptionCode errorCode;
    private Map<String, Object> data = new HashMap<String, Object>();

    public GeneralException() {
        super();
    }

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(Throwable cause) {
        super(cause);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionCode getErrorCode() {
        return errorCode;
    }

    public Object get(String name) {
        return data.get(name);
    }

    public GeneralException set(String name, Object obj) {
        data.put(name, obj);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String message = this.getLocalizedMessage();
        builder.append(getClass().getName())
                .append(StringUtils.isNotBlank(message) ? ": " + message : EMPTY_STRING)
                .append(ENDL);
        if (errorCode != null) {
            builder.append(errorCode.getClass().getName())
                    .append(": ")
                    .append(errorCode.toString())
                    .append(ENDL);
        }
        if (MapUtils.isNotEmpty(data)) {
            builder.append(data).append(ENDL);
        }
        return builder.toString();
    }

}
