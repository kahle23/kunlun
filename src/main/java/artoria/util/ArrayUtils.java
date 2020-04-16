package artoria.util;

import static artoria.common.Constants.ONE;
import static artoria.common.Constants.ZERO;

/**
 * Array tools.
 * @author Kahle
 */
public class ArrayUtils {

    public static boolean isEmpty(byte[] arr) {

        return arr == null || arr.length == ZERO;
    }

    public static boolean isNotEmpty(byte[] arr) {

        return arr != null && arr.length > ZERO;
    }

    public static <T> boolean isEmpty(T[] arr) {

        return arr == null || arr.length == ZERO;
    }

    public static <T> boolean isNotEmpty(T[] arr) {

        return arr != null && arr.length > ZERO;
    }

    public static boolean equal(byte[] bs1, byte[] bs2) {
        if (bs1 == null || bs2 == null) {
            return bs1 == null && bs2 == null;
        }
        if (bs1.length != bs2.length) {
            return false;
        }
        for (int i = ZERO; i < bs1.length; i++) {
            if (bs1[i] != bs2[i]) { return false; }
        }
        return true;
    }

    public static <T> boolean equal(T[] ts1, T[] ts2) {
        if (ts1 == null || ts2 == null) {
            return ts1 == null && ts2 == null;
        }
        if (ts1.length != ts2.length) {
            return false;
        }
        for (int i = ZERO; i < ts1.length; i++) {
            if (!ts1[i].equals(ts2[i])) { return false; }
        }
        return true;
    }

    public static void reverse(byte[] arr) {
        Assert.notNull(arr, "Parameter \"arr\" must not null. ");
        for (int start = ZERO, end = arr.length - 1; start < end; start++, end--) {
            byte temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
    }

    public static <T> void reverse(T[] arr) {
        Assert.notNull(arr, "Parameter \"arr\" must not null. ");
        for (int start = ZERO, end = arr.length - ONE; start < end; start++, end--) {
            T temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
    }

    /**
     * Get java bean array first not null element.
     * @param arr A java bean array
     * @param <T> Java bean type
     * @return A not null java bean
     */
    public static <T> T firstNotNullElement(T[] arr) {
        if (arr == null) { return null; }
        for (T bean : arr) {
            if (bean != null) { return bean; }
        }
        return null;
    }

}
