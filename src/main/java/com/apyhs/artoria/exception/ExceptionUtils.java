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
    private static final Map<Class<? extends ErrorCode>, Class<? extends GeneralException>> EXCEPTIONS;

    static {
        EXCEPTIONS = new ConcurrentHashMap<Class<? extends ErrorCode>, Class<? extends GeneralException>>();
        ExceptionUtils.register(ErrorCode.class, GeneralException.class);
    }

    private static Class<? extends GeneralException> find(ErrorCode errorCode) {
        Assert.notNull(errorCode, "Parameter \"errorCode\" must is not null. ");
        Class<? extends GeneralException> clazz = EXCEPTIONS.get(errorCode.getClass());
        if (clazz == null) { return GeneralException.class; }
        return clazz;
    }

    public static String toString(Throwable t) {
        if (t == null) { return null; }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    public static Class<? extends GeneralException> unregister(Class<? extends ErrorCode> codeClazz) {
        Assert.notNull(codeClazz, "Parameter \"codeClazz\" must is not null. ");
        Class<? extends GeneralException> remove = EXCEPTIONS.remove(codeClazz);
        log.info("Unregister: " + codeClazz.getName() + " >> " + remove.getName());
        return remove;
    }

    public static void register(Class<? extends ErrorCode> codeClazz, Class<? extends GeneralException> exceptionClazz) {
        Assert.notNull(codeClazz, "Parameter \"codeClazz\" must is not null. ");
        Assert.notNull(exceptionClazz, "Parameter \"exceptionClazz\" must is not null. ");
        EXCEPTIONS.put(codeClazz, exceptionClazz);
        log.info("Register: " + codeClazz.getName() + " >> " + exceptionClazz.getName());
    }

    public static GeneralException create(ErrorCode errorCode) {
        Class<? extends GeneralException> clazz = ExceptionUtils.find(errorCode);
        try {
            ReflectUtils ref = ReflectUtils.create(clazz);
            ref.newInstance((Object) errorCode.getContent());
            ref.callSetter("errorCode", errorCode);
            return (GeneralException) ref.getBean();
        }
        catch (ReflectionException e) {
            throw new UnexpectedException(e);
        }
    }

    public static GeneralException create(ErrorCode errorCode, Throwable t) {
        Class<? extends GeneralException> clazz = ExceptionUtils.find(errorCode);
        try {
            ReflectUtils ref = ReflectUtils.create(clazz);
            ref.newInstance(errorCode.getContent(), t);
            ref.callSetter("errorCode", errorCode);
            return (GeneralException) ref.getBean();
        }
        catch (ReflectionException e) {
            throw new UnexpectedException(e);
        }
    }

}
