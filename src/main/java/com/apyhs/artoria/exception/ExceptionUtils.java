package com.apyhs.artoria.exception;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Throwable tools.
 * @author Kahle
 */
public class ExceptionUtils {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static BusinessException create() {
        return new BusinessException();
    }

    public static BusinessException create(String message) {
        return new BusinessException(message);
    }

    public static BusinessException create(Throwable cause) {
        return new BusinessException(cause);
    }

    public static BusinessException create(String message, Throwable cause) {
        return new BusinessException(message, cause);
    }

    public static BusinessException create(ErrorCode errorCode) {
        Assert.notNull(errorCode, "Parameter \"errorCode\" must is not null. ");
        BusinessException e = new BusinessException(errorCode.getContent());
        e.setErrorCode(errorCode);
        return e;
    }

    public static BusinessException create(ErrorCode errorCode, Throwable cause) {
        Assert.notNull(errorCode, "Parameter \"errorCode\" must is not null. ");
        if (cause != null) {
            BusinessException e = new BusinessException(errorCode.getContent(), cause);
            e.setErrorCode(errorCode);
            return e;
        }
        else {
            return ExceptionUtils.create(errorCode);
        }
    }

    public static String toString(Throwable t) {
        if (t == null) { return null; }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
