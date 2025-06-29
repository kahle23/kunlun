/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.exception;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The exception tools.
 * @author Kahle
 */
public class ExceptionUtil {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtil.class);

//    public static RuntimeException wrap(Exception cause) {
//        boolean isRunEx = cause instanceof RuntimeException;
//        return isRunEx ? (RuntimeException) cause : new UncheckedException(cause);
//    }

    public static RuntimeException wrap(Throwable th) {
        // 如果是“错误”，直接抛出即可
        if (th instanceof Error) { throw (Error) th; }
        // 如果是“异常”，进行判断，运行异常直接返回，非运行异常需要转换
        boolean isRunEx = th instanceof RuntimeException;
        return isRunEx ? (RuntimeException) th : new UncheckedException(th);
    }

    public static String toString(Throwable t) {
        if (t == null) { return null; }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
