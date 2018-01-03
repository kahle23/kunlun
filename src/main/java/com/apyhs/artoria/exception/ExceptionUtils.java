package com.apyhs.artoria.exception;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ReflectUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Throwable tools.
 * @author Kahle
 */
public class ExceptionUtils {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);
    private static final Map<Class<? extends ErrorCode>, Class<? extends UncheckedException>> EXCEPTIONS;

    static {
        EXCEPTIONS = new ConcurrentHashMap<Class<? extends ErrorCode>, Class<? extends UncheckedException>>();
        ExceptionUtils.register(ErrorCode.class, UncheckedException.class);
    }

    public static Class<? extends UncheckedException> unregister(Class<? extends ErrorCode> codeClazz) {
        Assert.notNull(codeClazz, "Parameter \"codeClazz\" must is not null. ");
        Class<? extends UncheckedException> remove = EXCEPTIONS.remove(codeClazz);
        log.info("Unregister: " + codeClazz.getName() + " >> " + remove.getName());
        return remove;
    }

    public static void register(Class<? extends ErrorCode> codeClazz, Class<? extends UncheckedException> exceptionClazz) {
        Assert.notNull(codeClazz, "Parameter \"codeClazz\" must is not null. ");
        Assert.notNull(exceptionClazz, "Parameter \"exceptionClazz\" must is not null. ");
        EXCEPTIONS.put(codeClazz, exceptionClazz);
        log.info("Register: " + codeClazz.getName() + " >> " + exceptionClazz.getName());
    }

    public static UncheckedException create() {
        return new UncheckedException();
    }

    public static UncheckedException create(String message) {
        return new UncheckedException(message);
    }

    public static UncheckedException create(Throwable cause) {
        return new UncheckedException(cause);
    }

    public static UncheckedException create(String message, Throwable cause) {
        return new UncheckedException(message, cause);
    }

    public static UncheckedException create(ErrorCode errorCode) {
        return ExceptionUtils.create(errorCode, null);
    }

    public static UncheckedException create(ErrorCode errorCode, Throwable t) {
        Assert.notNull(errorCode, "Parameter \"errorCode\" must is not null. ");
        Class<? extends UncheckedException> clazz = EXCEPTIONS.get(errorCode.getClass());
        if (clazz == null) { clazz = UncheckedException.class; }
        try {
            ReflectUtils ref = ReflectUtils.create(clazz);
            if (t != null) {
                ref.newInstance(errorCode.getContent(), t);
            }
            else {
                ref.newInstance((Object) errorCode.getContent());
            }
            ref.call("setErrorCode", errorCode);
            return (UncheckedException) ref.getBean();
        }
        catch (ReflectionException e) {
            throw new UnexpectedException(e);
        }
    }

    public static void check(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw ExceptionUtils.create(errorCode);
        }
    }

    public static void check(boolean expression, ErrorCode errorCode, String others) {
        if (!expression) {
            throw ExceptionUtils.create(errorCode).setOthers(others);
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
