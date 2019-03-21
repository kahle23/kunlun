package artoria.exception;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception tools.
 * @author Kahle
 */
public class ExceptionUtils {
    private static Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static RuntimeException wrap(Exception cause) {
        boolean isRE = cause instanceof RuntimeException;
        return isRE ? (RuntimeException) cause : new UncheckedException(cause);
    }

    public static RuntimeException wrap(Exception cause, Class<? extends RuntimeException> clazz) {
        try {
            if (cause instanceof RuntimeException) {
                return (RuntimeException) cause;
            }
            return ReflectUtils.newInstance(clazz, Throwable.class);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
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
