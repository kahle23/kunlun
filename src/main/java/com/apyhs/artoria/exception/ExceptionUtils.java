package com.apyhs.artoria.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Throwable tools.
 * @author Kahle
 */
public class ExceptionUtils {

    public static String toString(Throwable t) {
        if (t == null) { return null; }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
