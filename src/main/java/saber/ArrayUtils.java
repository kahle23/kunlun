package saber;

import java.security.SecureRandom;
import java.util.Random;

public class ArrayUtils {
    private static final Random RANDOM = new SecureRandom();

    public static boolean isEmpty(byte[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(byte[] arr) {
        return arr != null && arr.length > 0;
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] arr) {
        return arr != null && arr.length > 0;
    }

    public static boolean equal(byte[] a, byte[] b) {
        if (a == null ? b == null : b != null && a == b) return true;
        if (a.length != b.length) return false;//TODO
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    public static <T> T[] reverse(T[] arr) {
        for (int start = 0, end = arr.length - 1; start < end; start++, end--) {
            T temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
        return arr;
    }

    public static <T> T[] confuse(T[] arr) {
        for (int i = arr.length - 1; i > 1; --i) {
            int nextInt = RANDOM.nextInt(i);
            T tmp = arr[nextInt];
            arr[nextInt] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

}
