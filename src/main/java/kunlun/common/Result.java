/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import kunlun.common.constant.Nil;
import kunlun.data.CodeDefinition;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

import java.io.Serializable;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static kunlun.util.Assert.notBlank;
import static kunlun.util.Assert.notNull;

/**
 * The uniform result output object.
 * @param <T> The data type
 * @author Kahle
 */
public class Result<T> implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Result.class);
    private static volatile CodeDefinition defaultSuccessCode;
    private static volatile CodeDefinition defaultFailureCode;

    public static CodeDefinition getDefaultSuccessCode() {
        if (defaultSuccessCode != null) { return defaultSuccessCode; }
        synchronized (Result.class) {
            if (defaultSuccessCode != null) { return defaultSuccessCode; }
            Result.setDefaultSuccessCode(Errors.ok);
            return defaultSuccessCode;
        }
    }

    public static void setDefaultSuccessCode(CodeDefinition defaultSuccessCode) {
        notBlank(notNull(defaultSuccessCode).getDescription()
                , "Parameter \"defaultSuccessCode.description\" must not blank. ");
        notNull(defaultSuccessCode.getCode()
                , "Parameter \"defaultSuccessCode.code\" must not null. ");
        log.debug("Set default success code: {}", defaultSuccessCode.getClass().getName());
        Result.defaultSuccessCode = defaultSuccessCode;
    }

    public static CodeDefinition getDefaultFailureCode() {
        if (defaultFailureCode != null) { return defaultFailureCode; }
        synchronized (Result.class) {
            if (defaultFailureCode != null) { return defaultFailureCode; }
            Result.setDefaultFailureCode(Errors.internalServerError);
            return defaultFailureCode;
        }
    }

    public static void setDefaultFailureCode(CodeDefinition defaultFailureCode) {
        notBlank(notNull(defaultFailureCode).getDescription()
                , "Parameter \"defaultFailureCode.description\" must not blank. ");
        notNull(defaultFailureCode.getCode()
                , "Parameter \"defaultFailureCode.code\" must not null. ");
        log.debug("Set default failure code: {}", defaultFailureCode.getClass().getName());
        Result.defaultFailureCode = defaultFailureCode;
    }


    private Boolean success;
    private Object code;
    private String message;
    private T data;

    public Result(Boolean success, Object code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result() {

    }

    public Boolean getSuccess() {

        return success;
    }

    public void setSuccess(Boolean success) {

        this.success = success;
    }

    public Object getCode() {

        return code;
    }

    public void setCode(Object code) {

        this.code = code;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public T getData() {

        return data;
    }

    public void setData(T data) {

        this.data = data;
    }

    public static <T> Result<T> success(String message, T data) {

        return new Result<T>(TRUE, getDefaultSuccessCode().getCode(), message, data);
    }

    public static <T> Result<T> success(T data) {

        return success(getDefaultSuccessCode().getDescription(), data);
    }

    public static <T> Result<T> success() {

        return success(null);
    }

    public static <T> Result<T> success(CodeDefinition code, T data) {

        return new Result<T>(TRUE, code.getCode(), code.getDescription(), data);
    }

    public static <T> Result<T> failure(Object code, String message) {

        return new Result<T>(FALSE, code, message, Nil.<T>g());
    }

    public static <T> Result<T> failure(String message) {

        return failure(getDefaultFailureCode().getCode(), message);
    }

    public static <T> Result<T> failure() {

        return failure(getDefaultFailureCode().getDescription());
    }

    public static <T> Result<T> failure(CodeDefinition code) {

        return failure(code.getCode(), code.getDescription());
    }

}
