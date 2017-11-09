package saber.util;

/**
 * @author Kahle
 */
public class ThreadUtils {

    public static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int currentLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static String currentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static String currentClassName() {
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }

    public static String currentFileName() {
        return Thread.currentThread().getStackTrace()[2].getFileName();
    }

    public static String currentThreadName() {
        return Thread.currentThread().getName();
    }

}
