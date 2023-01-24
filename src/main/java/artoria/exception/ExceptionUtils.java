package artoria.exception;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The exception tools.
 * @author Kahle
 */
public class ExceptionUtils {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static RuntimeException wrap(Exception cause) {
        boolean isRunEx = cause instanceof RuntimeException;
        return isRunEx ? (RuntimeException) cause : new UncheckedException(cause);
    }

    public static String toString(Throwable t) {
        if (t == null) { return null; }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
