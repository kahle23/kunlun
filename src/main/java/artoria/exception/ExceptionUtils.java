package artoria.exception;

import artoria.reflect.ReflectUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

/**
 * Exception tools.
 * @author Kahle
 */
public class ExceptionUtils {
    private static Logger log = Logger.getLogger(ExceptionUtils.class.getName());

    public static String toString(Throwable t) {
        if (t == null) { return null; }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    public static RuntimeException wrap(Exception cause) {
        boolean isRE = cause instanceof RuntimeException;
        return isRE ? (RuntimeException) cause : new UncheckedException(cause);
    }

    public static RuntimeException wrap(Exception cause, Class<? extends RuntimeException> clazz) {
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        }
        try {
            Constructor<?> cstr = ReflectUtils.findConstructor(clazz, Throwable.class);
            return (RuntimeException) cstr.newInstance(cause);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
