package artoria.util;

/**
 * @author Kahle
 */
public class ArrayUtils {

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
        for (int start = 0, end = arr.length - 1; start < end; start++, end--) {
            byte temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
        return arr;
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] arr) {
        return arr != null && arr.length > 0;
    }

    public static <T> T[] reverse(T[] arr) {
        for (int start = 0, end = arr.length - 1; start < end; start++, end--) {
            T temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
        return arr;
    }

}
