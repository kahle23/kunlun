package com.apyhs.artoria.exception;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Throwable tools.
 * @author Kahle
 */
public class ExceptionUtils {
    private static Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static BusinessException wrap(Throwable cause) {
        return cause instanceof BusinessException
                ? (BusinessException) cause : new BusinessException(cause);
    }

    public static BusinessException wrap(Throwable cause, ErrorCode code) {
        if (code != null) {
            BusinessException e = (BusinessException) cause;
            return code.equals(e.getErrorCode())
                    ? e : new BusinessException(code, cause);
        }
        else {
            return ExceptionUtils.wrap(cause);
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
