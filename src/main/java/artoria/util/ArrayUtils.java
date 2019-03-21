package artoria.util;

/**
 * Array tools.
 * @author Kahle
 */
public class ArrayUtils {

    /**
     * Get java bean array first not null element.
     * @param arr A java bean array
     * @param <T> Java bean type
     * @return A not null java bean
     */
    public static <T> T getFirstNotNullElement(T[] arr) {
        if (arr == null) { return null; }
        for (T bean : arr) {
            if (bean != null) { return bean; }
        }
        return null;
    }

    public static boolean equal(byte[] bytes1, byte[] bytes2) {
        if (bytes1 == null || bytes2 == null) {
            return bytes1 == null && bytes2 == null;
        }
        if (bytes1.length != bytes2.length) {
            return false;
        }
        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i]) { return false; }
        }
        return true;
    }

    public static boolean isEmpty(byte[] arr) {

        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(byte[] arr) {

        return arr != null && arr.length > 0;
    }

    public static byte[] reverse(byte[] arr) {
        Assert.notNull(arr, "Parameter \"arr\" must not null. ");
        for (int start = 0, end = arr.length - 1; start < end; start++, end--) {
            byte temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
        return arr;
    }

    public static <T> boolean equal(T[] ts1, T[] ts2) {
        if (ts1 == null || ts2 == null) {
            return ts1 == null && ts2 == null;
        }
        if (ts1.length != ts2.length) {
            return false;
        }
        for (int i = 0; i < ts1.length; i++) {
            if (!ts1[i].equals(ts2[i])) { return false; }
        }
        return true;
    }

    public static <T> boolean isEmpty(T[] arr) {

        return arr == null || arr.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] arr) {

        return arr != null && arr.length > 0;
    }

    public static <T> T[] reverse(T[] arr) {
        Assert.notNull(arr, "Parameter \"arr\" must not null. ");
        for (int start = 0, end = arr.length - 1; start < end; start++, end--) {
            T temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
        return arr;
    }

}
