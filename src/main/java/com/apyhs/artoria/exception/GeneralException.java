package com.apyhs.artoria.exception;

import com.apyhs.artoria.util.MapUtils;
import com.apyhs.artoria.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.apyhs.artoria.util.StringConstant.ENDL;

/**
 * General exception.
 * @author Kahle
 */
public class GeneralException extends RuntimeException {

    private ErrorCode errorCode;
    private String remark;
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

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    protected void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getRemark() {
        return remark;
    }

    public GeneralException setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public GeneralException set(String name, Object obj) {
        data.put(name, obj);
        return this;
    }

    public GeneralException set(Map<String, Object> data) {
        this.data.putAll(data);
        return this;
    }

    public String summary() {
        StringBuilder builder = new StringBuilder(ENDL);
        if (errorCode != null) {
            builder.append("Type:    ")
                    .append(errorCode.getClass().getSimpleName())
                    .append(ENDL);
            builder.append("Code:    ").append(errorCode.getCode())
                    .append(ENDL);
            builder.append("Content: ").append(errorCode.getContent())
                    .append(ENDL);
        }
        if (StringUtils.isNotBlank(remark)) {
            builder.append("Remark:  ").append(remark)
                    .append(ENDL);
        }
        if (MapUtils.isNotEmpty(data)) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = StringUtils.capitalize(entry.getKey());
                builder.append(key).append(": ").append(entry.getValue())
                        .append(ENDL);
            }
        }
        return builder.toString();
    }

}
