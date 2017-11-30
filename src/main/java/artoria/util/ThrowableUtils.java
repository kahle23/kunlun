package artoria.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Kahle
 */
public class ThrowableUtils {

    public static String toString(Throwable t) {
        Assert.notNull(t);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
