package saber;

public abstract class ThreadUtils {

    public static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int lineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static String methodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static String className() {
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }

    public static String fileName() {
        return Thread.currentThread().getStackTrace()[2].getFileName();
    }

    public static String threadName() {
        return Thread.currentThread().getName();
    }

}
